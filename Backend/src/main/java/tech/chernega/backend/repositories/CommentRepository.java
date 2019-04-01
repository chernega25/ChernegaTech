package tech.chernega.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.chernega.backend.entities.Comment;
import tech.chernega.backend.entities.Post;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Collection<Comment> findAllByPostIdOrderByCreatedTimeDesc(Long postId);
}
