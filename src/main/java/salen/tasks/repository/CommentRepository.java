package salen.tasks.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import salen.tasks.entity.Comment;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    void deleteByIdAndAuthor_id(Long id, Long authorId);

    Optional<Comment> findByIdAndTask_Id(Long id, Long taskId);

    Page<Comment> findAllByTask_Id(Long taskId, Pageable pageable);

    void deleteByIdAndAuthor_idAndTask_Id(Long id, Long authorId, Long taskId);
}
