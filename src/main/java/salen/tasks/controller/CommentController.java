package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.Comment;
import salen.tasks.entity.Role;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.entity.dto.CommentDto;
import salen.tasks.entity.mapper.CommentMapper;
import salen.tasks.exception.CommentAlreadyExistsException;
import salen.tasks.exception.CommentNotFoundException;
import salen.tasks.exception.TaskNotFoundException;
import salen.tasks.service.CommentService;
import salen.tasks.service.TaskService;
import salen.tasks.service.UserService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper mapper;
    private final UserService userService;
    private final TaskService taskService;

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable Long taskId, @PathVariable Long id) {
        return commentService.get(id, taskId).map(mapper::toDto).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @GetMapping
    public Page<CommentDto> getAll(@PathVariable Long taskId, @RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size) {
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(10));
        return commentService.getAll(taskId, pageable).map(mapper::toDto);
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable Long taskId, @RequestBody @Valid CommentDto dto, Principal principal) {
        User user = userService.getByEmail(principal.getName()).get();
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

        if (dto.getId() != null && commentService.get(dto.getId(), taskId).isPresent())
            throw new CommentAlreadyExistsException(dto.getId());
        Comment comment;

        if (user.getRoles().contains(Role.ADMIN) || user.getId().equals(task.getExecutor().getId())) {
            comment = commentService.save(mapper.toEntity(dto), user, task);
        } else throw new AccessDeniedException("Access denied");

        return ResponseEntity.ok().body(mapper.toDto(comment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long taskId, @PathVariable Long id, Principal principal) {
        User user = userService.getByEmail(principal.getName()).get();
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));

        if (user.getRoles().contains(Role.ADMIN) || user.getId().equals(task.getExecutor().getId())) {
            commentService.delete(id, user.getEmail(), taskId);
        } else throw new AccessDeniedException("Access denied");

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable Long taskId, @PathVariable Long id, @RequestBody @Valid CommentDto dto, Principal principal) {
        User user = userService.getByEmail(principal.getName()).get();
        Task task = taskService.get(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        Comment updatedComment;

        if (user.getRoles().contains(Role.ADMIN) || user.getId().equals(task.getExecutor().getId())) {
            updatedComment = commentService.get(id, taskId).map((comment) -> {
                comment.setValue(dto.getValue());
                return comment;
            }).orElseThrow(() -> new CommentNotFoundException(id));
        } else throw new AccessDeniedException("Access denied");

        return mapper.toDto(commentService.save(updatedComment, user, task));
    }
}
