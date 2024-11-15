package salen.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import salen.tasks.entity.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByIdAndAuthor_id(Long id, Long authorId);
}
