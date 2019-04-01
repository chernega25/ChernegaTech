package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class AuthorizationController extends AbstractController{

    private final UserRepository userRepository;

    @Autowired
    public AuthorizationController(UserRepository userRepository) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody String body) {
        Credentials user = new Gson().fromJson(body, Credentials.class);
        if (userRepository.findByUsername(user.username).isEmpty()) {
            String response = "{\"id\": \""
                    + userRepository.save(new User(user.username, user.password, Role.ROLE_ADMIN)).getId()
                    + "\"}";
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
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
            User currentUser = (User) users.toArray()[0];
            UserRole userRole = new UserRole(currentUser.getId(), currentUser.getRole());
            String response = "{\"token\": \"" + newToken(userRole) + "\"}";
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(@RequestHeader("Token") String token) {
        return new ResponseEntity<>(removeToken(token)
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED
        );
    }

}
