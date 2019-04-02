package tech.chernega.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.chernega.backend.entities.Post;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {
    public Collection<Post> findAllByOrderByCreatedTimeDesc();
}
