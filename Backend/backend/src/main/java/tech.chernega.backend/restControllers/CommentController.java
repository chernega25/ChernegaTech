package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.chernega.backend.utils.Content;
import tech.chernega.backend.utils.Role;
import tech.chernega.backend.utils.UserRole;
import tech.chernega.backend.entities.Comment;
import tech.chernega.backend.repositories.CommentRepository;

import java.time.Instant;
import java.util.Collection;

@RestController
@RequestMapping("/comments")
public class CommentController extends AbstractController {

    private CommentRepository commentRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository) {
        super();
        this.commentRepository = commentRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getComment(@PathVariable String id) {
        return get(id, commentRepository);
    }

    @GetMapping("")
    public ResponseEntity<String> getPostComments(@RequestParam("post") long id) {
        Collection<Comment> comments = commentRepository.findAllByPostIdOrderByCreatedTimeAsc(id);
        String response = new Gson().toJson(comments);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> addComment(@RequestParam("post") long id,
                                             @RequestHeader("Token") String token,
                                             @RequestBody String body) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Content content = new Gson().fromJson(body, Content.class);
            Comment comment = new Comment(content.body, user.id, id, Instant.now());
            String response = "{\"id\": \"" + commentRepository.save(comment).getId() + "\"}";
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> changeComment(@PathVariable String id,
                                                @RequestHeader("Token") String token,
                                                @RequestBody String body) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Comment comment = commentRepository.findById(Long.valueOf(id)).orElse(null);
            if (comment == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (comment.getUserId() == user.id || user.role == Role.ROLE_ADMIN) {
                Content newContent = new Gson().fromJson(body, Content.class);
                comment.setBody(newContent.body);
                commentRepository.save(comment);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable String id, @RequestHeader("Token") String token) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            Comment comment = commentRepository.findById(Long.valueOf(id)).orElse(null);
            if (comment == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else if (comment.getUserId() == user.id || user.role == Role.ROLE_ADMIN) {
                commentRepository.delete(comment);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
    }

}
