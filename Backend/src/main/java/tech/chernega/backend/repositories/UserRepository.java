package tech.chernega.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.chernega.backend.entities.UserEntity;

import java.util.Collection;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Collection<UserEntity> findByLogin(String login);
}
