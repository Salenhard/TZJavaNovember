package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salen.tasks.entity.Comment;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.exception.TaskNotFoundException;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.repository.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final UserService userService;
    private final TaskService taskService;

    public Optional<Comment> get(Long id) {
        return repository.findById(id);
    }

    public List<Comment> getAll() {
        return repository.findAll();
    }

    public void delete(Long id, Long authorId) {
        repository.deleteByIdAndAuthor_id(id, authorId);
    }

    public Comment save(Comment comment, Long authorId, Long taskId) {
        User author = userService.get(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        comment.setAuthor(author);
        comment.setTask(task);
        return repository.save(comment);
    }

}

