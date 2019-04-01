package tech.chernega.backend.restControllers;

import com.google.gson.Gson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import tech.chernega.backend.data.TokenToUser;
import tech.chernega.backend.data.UserRole;

import java.util.UUID;

public class AbstractController {
    private TokenToUser tokenToUser = TokenToUser.getInstance();

    UserRole getUserByToken(String token) {
        return tokenToUser.map.get(token);
    }

    String newToken(UserRole userRole) {
        String token = UUID.randomUUID().toString();
        tokenToUser.map.put(token, userRole);
        return token;
    }

    Boolean removeToken(String token) {
        return tokenToUser.map.remove(token) != null;
    }

    HttpHeaders headers = new HttpHeaders();

    protected AbstractController() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    <T> ResponseEntity<String> get(String id, JpaRepository<T, Long> repository) {
        T t = repository.findById(Long.valueOf(id)).orElse(null);
        if (t == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            String response = new Gson().toJson(t);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
    }

}
