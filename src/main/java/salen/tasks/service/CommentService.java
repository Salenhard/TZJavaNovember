package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salen.tasks.entity.Comment;
import salen.tasks.entity.Role;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.exception.CommentNotFoundException;
import salen.tasks.exception.TaskNotFoundException;
import salen.tasks.repository.CommentRepository;

import java.util.Objects;
import java.util.Optional;

import static salen.tasks.util.CommentSpecification.equalTaskId;
import static salen.tasks.util.CommentSpecification.likeAuthor;

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

    public Page<Comment> getAll(Long taskId, String author, Pageable pageable) {
        Specification<Comment> filters = Specification.where(equalTaskId(taskId))
                .and(Objects.isNull(author) ? null : likeAuthor(author));

        return repository.findAll(filters, pageable);
    }

    @CacheEvict(value = "comments", key = "#id")
    @Transactional
    public void delete(Long id, String authorEmail, Long taskId) {
        User user = userService.getByEmail(authorEmail).get();
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

        if (!user.getRoles().contains(Role.ADMIN) && !user.getId().equals(task.getExecutor().getId()))
            throw new AccessDeniedException("Access denied");

        repository.deleteById(id);
    }

    @CachePut(value = "comments", key = "#comment.id")
    @Transactional
    public Comment save(Comment comment, String authorName, Long taskId) {
        User user = userService.getByEmail(authorName).get();
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

        if (comment.getId() != null && repository.findByIdAndTask_Id(comment.getId(), taskId).isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment with id:%d not found".formatted(comment.getId()));

        if (!user.getRoles().contains(Role.ADMIN) && !user.getId().equals(task.getExecutor().getId()))
            throw new AccessDeniedException("Access denied");

        comment.setAuthor(user);
        comment.setTask(task);

        return repository.save(comment);
    }

    @CachePut(value = "comments", key = "#comment.id")
    @Transactional
    public Comment update(Long id, Comment comment, String authorName, Long taskId) {
        User user = userService.getByEmail(authorName).get();
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        Comment updatedComment;

        if (!user.getRoles().contains(Role.ADMIN) && !user.getId().equals(task.getExecutor().getId()))
            throw new AccessDeniedException("Access denied");

        updatedComment = repository.findByIdAndTask_Id(id, taskId).map((newComment) -> {
            newComment.setValue(comment.getValue());
            return comment;
        }).orElseThrow(() -> new CommentNotFoundException(id));

        return repository.save(updatedComment);
    }
}

