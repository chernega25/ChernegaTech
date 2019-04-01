package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.chernega.backend.data.TokenToUser;
import tech.chernega.backend.data.UserRole;
import tech.chernega.backend.repositories.UserRepository;
import tech.chernega.backend.data.Role;
import tech.chernega.backend.data.Credentials;
import tech.chernega.backend.entities.User;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {

    private TokenToUser tokenToUser = TokenToUser.getInstance();
    private final UserRepository userRepository;

    @Autowired
    public AuthorizationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody String body) {
        Credentials user = new Gson().fromJson(body, Credentials.class);
        if (userRepository.findByUsername(user.username).isEmpty()) {
            String response = "{\"id\": \""
                    + userRepository.save(new User(user.username, user.password, Role.ROLE_ADMIN)).getId()
                    + "\"}";
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody String body) {
        Credentials user = new Gson().fromJson(body, Credentials.class);
        Collection<User> users = userRepository.findByUsername(user.username);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String token = UUID.randomUUID().toString();
            User currentUser = (User) users.toArray()[0];
            UserRole userRole = new UserRole(currentUser.getId(), currentUser.getRole());
            tokenToUser.map.put(token, userRole);
            String response = "{\"token\": \"" + token + "\"}";
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(@RequestHeader("Token") String token) {
        if (tokenToUser.map.remove(token) == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

}
