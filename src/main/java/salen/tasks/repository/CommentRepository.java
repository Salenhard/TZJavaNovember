package salen.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import salen.tasks.entity.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Optional<Comment> findByIdAndTask_Id(Long id, Long taskId);

    void deleteByIdAndAuthor_EmailAndTask_Id(Long id, String authorEmail, Long taskId);
}
