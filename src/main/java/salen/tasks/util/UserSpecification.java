package salen.tasks.util;

import org.springframework.data.jpa.domain.Specification;
import salen.tasks.entity.User;

public class UserSpecification {
    private UserSpecification() {
    }

    public static Specification<User> isActive(boolean isActive) {
        return (root, query, builder) -> builder.equal(root.get("isActive"), isActive);
    }
}
