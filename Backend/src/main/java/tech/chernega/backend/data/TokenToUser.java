package tech.chernega.backend.data;

import java.util.HashMap;
import java.util.Map;

public class TokenToUser {
    private static TokenToUser ourInstance = new TokenToUser();

    public static TokenToUser getInstance() {
        return ourInstance;
    }

    private TokenToUser() {
    }

    public Map<String, UserRole> map = new HashMap<>();
}
