package salen.tasks.util;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;

public class TaskSpecification {
    private TaskSpecification() {
    }

    public static Specification<Task> likeAuthorEmail(String authorEmail) {
        return (root, query, builder) ->
        {
            Join<User, Task> taskAuthor = root.join("author");
            return builder.like(taskAuthor.get("email"), "%" + authorEmail + "%");
        };
    }

    public static Specification<Task> likeExecutorEmail(String executorEmail) {
        return (root, query, builder) ->
        {
            Join<User, Task> taskAuthor = root.join("executor");
            return builder.like(taskAuthor.get("email"), "%" + executorEmail + "%");
        };
    }


    public static Specification<Task> likeTitle(String title) {
        return (root, query, builder) ->
        {
            return builder.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<Task> equalStatus(String status) {
        return ((root, query, builder) ->
        {
            return builder.equal(root.get("status"), status);
        });
    }

    public static Specification<Task> equalPriority(String priority) {
        return ((root, query, builder) ->
        {
            return builder.equal(root.get("priority"), priority);
        });
    }

}
