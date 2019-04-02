package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.chernega.backend.utils.Content;
import tech.chernega.backend.utils.Role;
import tech.chernega.backend.utils.UserRole;
import tech.chernega.backend.entities.Post;
import tech.chernega.backend.repositories.PostRepository;

import java.time.Instant;
import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController extends AbstractController {

    private PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository) {
        super();
        this.postRepository = postRepository;
    }

    @GetMapping("")
    public ResponseEntity<String> getAllPosts() {
        Collection<Post> posts = postRepository.findAllByOrderByCreatedTimeDesc();
        String response = new Gson().toJson(posts);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> createPost(@RequestHeader("Token") String token, @RequestBody String body) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (user.role == Role.ROLE_ADMIN) {
            Content content = new Gson().fromJson(body, Content.class);
            Post post = new Post(content.header, content.body, user.id, Instant.now());
            String response = "{\"id\": \"" + postRepository.save(post).getId() + "\"}";
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPost(@PathVariable String id) {
        return get(id, postRepository);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> changePost(@PathVariable String id,
                                          @RequestHeader("Token") String token,
                                          @RequestBody String body) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (user.role == Role.ROLE_ADMIN) {
            Post post = postRepository.findById(Long.valueOf(id)).orElse(null);
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Content newContent = new Gson().fromJson(body, Content.class);
                post.setHeader(newContent.header);
                post.setBody(newContent.body);
                postRepository.save(post);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id, @RequestHeader("Token") String token) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (user.role == Role.ROLE_ADMIN) {
            postRepository.deleteById(Long.valueOf(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
