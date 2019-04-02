package tech.chernega.backend.restControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.chernega.backend.utils.UserRole;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    @GetMapping("")
    public ResponseEntity<String> getCurrentUser(@RequestHeader("Token") String token) {
        UserRole user = getUserByToken(token);
        if (user == null) {
            return new ResponseEntity<>()
        }
    }
}
