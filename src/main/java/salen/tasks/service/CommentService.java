package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import salen.tasks.entity.Comment;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.exception.TaskNotFoundException;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.repository.CommentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private final TaskService taskService;

    @Cacheable(value = "comments", key = "#id")
    public Optional<Comment> get(Long id, Long taskId) {
        return repository.findByIdAndTask_Id(id, taskId);
    }

    public Page<Comment> getAll(Long taskId, Pageable pageable) {
        return repository.findAllByTask_Id(taskId, pageable);
    }

    @CacheEvict(value = "comments", key = "#id")
    public void delete(Long id, String authorEmail, Long taskId) {
        repository.deleteByIdAndAuthor_EmailAndTask_Id(id, authorEmail, taskId);
    }

    @CachePut(value = "comments", key = "#comment.id")
    public Comment save(Comment comment, String authorEmail, Long taskId) {
        User author = userService.getByEmail(authorEmail).orElseThrow(() -> new UserNotFoundException(authorEmail));
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        comment.setAuthor(author);
        comment.setTask(task);
        return repository.save(comment);
    }

}

