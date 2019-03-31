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
import tech.chernega.backend.data.UserAuthInfo;
import tech.chernega.backend.entities.UserEntity;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class LoginController {

    private TokenToUser tokenToUser = TokenToUser.getInstance();
    private final UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody String body) {
        UserAuthInfo user = new Gson().fromJson(body, UserAuthInfo.class);
        if (userRepository.findByLogin(user.login).isEmpty()) {
            String response = "{\"id\": \""
                    + userRepository.save(new UserEntity(user.login, user.password, Role.ROLE_ADMIN)).getId()
                    + "\"}";
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody String body) {
        UserAuthInfo user = new Gson().fromJson(body, UserAuthInfo.class);
        Collection<UserEntity> users = userRepository.findByLogin(user.login);
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            String token = UUID.randomUUID().toString();
            UserEntity currentUser = (UserEntity) users.toArray()[0];
            UserRole userRole = new UserRole(currentUser.getId(), currentUser.getRole());
            tokenToUser.map.put(token, userRole);
            String response = String.format("{\"token\": \"%s\"}", token);
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
