package tech.chernega.backend.data;

public class UserRole {
    public long id;
    public Role role;

    public UserRole(long id, Role role) {
        this.id = id;
        this.role = role;
    }
}
