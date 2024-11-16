package salen.tasks.util;

import org.springframework.data.jpa.domain.Specification;
import salen.tasks.entity.Comment;

public class CommentSpecification {
    private CommentSpecification() {
    }

    public static Specification<Comment> taskId(Long taskId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("task_id"), taskId);
        };
    }
}
