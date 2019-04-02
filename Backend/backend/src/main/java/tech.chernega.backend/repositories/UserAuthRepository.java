package tech.chernega.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.chernega.backend.entities.UserAuth;

import java.util.Collection;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    Collection<UserAuth> findByUsername(String username);
}
