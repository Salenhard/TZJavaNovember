package salen.tasks.util;

import org.springframework.data.jpa.domain.Specification;
import salen.tasks.entity.Comment;

public class CommentSpecification {
    private CommentSpecification() {
    }

    public static Specification<Comment> likeAuthor(String author) {
        return ((root, query, builder) ->
                builder.like(root.get("author").get("email"), "%" + author + "%"));
    }

    public static Specification<Comment> equalTaskId(Long taskId) {
        return ((root, query, builder) ->
                builder.equal(root.get("task").get("id"), taskId));
    }
}
