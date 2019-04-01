package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.chernega.backend.data.PostContent;
import tech.chernega.backend.data.Role;
import tech.chernega.backend.data.TokenToUser;
import tech.chernega.backend.data.UserRole;
import tech.chernega.backend.entities.Post;
import tech.chernega.backend.repositories.PostRepository;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private TokenToUser tokenToUser = TokenToUser.getInstance();
    private PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping("/")
    public ResponseEntity<String> newPost(@RequestHeader("Token") String token, @RequestBody String body) {
        UserRole user = tokenToUser.map.getOrDefault(token, null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (user.role == Role.ROLE_ADMIN) {
            PostContent postContent = new Gson().fromJson(body, PostContent.class);
            Post post = new Post(postContent.header, postContent.body, user.id, Instant.now());
            String response = "{\"id\": \"" + postRepository.save(post).getId() + "\"}";
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPost(@PathVariable String id) {
        Post post = postRepository.findById(Long.valueOf(id)).orElse(null);
        if (post == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            String response = new Gson().toJson(post);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> newPost(@PathVariable String id,
                                          @RequestHeader("Token") String token,
                                          @RequestBody String body) {
        UserRole user = tokenToUser.map.getOrDefault(token, null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else if (user.role == Role.ROLE_ADMIN) {
            Post post = postRepository.findById(Long.valueOf(id)).orElse(null);
            if (post == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                PostContent newPostContent = new Gson().fromJson(body, PostContent.class);
                post.setHeader(newPostContent.header);
                post.setBody(newPostContent.body);
                postRepository.save(post);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable String id, @RequestHeader("Token") String token) {
        UserRole user = tokenToUser.map.getOrDefault(token, null);
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
