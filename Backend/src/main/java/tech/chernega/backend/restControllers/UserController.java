package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.chernega.backend.repositories.UserRepository;
import tech.chernega.backend.data.Role;
import tech.chernega.backend.data.UserAuthInfo;
import tech.chernega.backend.entities.UserEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private Map<String, UserEntity> tokenToUser = new HashMap<>();
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody String body) {
        UserAuthInfo user = new Gson().fromJson(body, UserAuthInfo.class);
        if (userRepository.findByLogin(user.login).isEmpty()) {
            String response = String.format("{\"id\": \"%s\"}",
                    userRepository.save(new UserEntity(user.login, user.password, Role.ROLE_ADMIN)).getId());
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
            tokenToUser.put(token, (UserEntity) users.toArray()[0]);
            String response = String.format("{\"token\": \"%s\"}", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signout(@RequestHeader("Token") String token) {
        if (tokenToUser.containsKey(token)) {
            tokenToUser.remove(token);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
