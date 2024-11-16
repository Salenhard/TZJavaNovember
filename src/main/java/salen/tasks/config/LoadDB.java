package salen.tasks.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import salen.tasks.entity.*;
import salen.tasks.service.CommentService;
import salen.tasks.service.TaskService;
import salen.tasks.service.UserService;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class LoadDB {
    private final UserService userService;
    private final CommentService commentService;
    private final TaskService taskService;
    private final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner load() {
        return args -> {
            userService.save(new User(1L, "email@mail.com", "e@1234", Set.of(Role.ADMIN), true));
            userService.save(new User(2L, "ame@mail.com", "a@1234", Set.of(Role.USER), true));
            taskService.save(new Task(1L, "test", "task for test", Status.WAITING, Priority.HIGH, userService.get(1L).get(), userService.get(2L).get()));
            Comment comment = new Comment();
            comment.setValue("test comment");
            commentService.save(comment, "email@mail.com", 1L);
            userService.getAll().forEach((entity) -> log.info("preload... {}", entity));
        };
    }
}
