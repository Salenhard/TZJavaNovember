package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.*;
import salen.tasks.entity.dto.TaskDto;
import salen.tasks.entity.mapper.TaskMapper;
import salen.tasks.exception.TaskAlreadyExistsException;
import salen.tasks.exception.TaskNotFoundException;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.service.TaskService;
import salen.tasks.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper mapper;
    private final UserService userService;

    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return taskService.get(id).map(mapper::toDto).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @GetMapping
    public List<TaskDto> getAll() {
        return taskService.getAll().stream().map(mapper::toDto).toList();
    }

    @PostMapping
    @Secured("ADMIN")
    public ResponseEntity<?> save(@RequestBody @Valid TaskDto dto, Principal principal) {
        User user = userService.getByEmail(principal.getName()).get();
        User executor = userService.get(dto.getExecutorId()).orElseThrow(() -> new UserNotFoundException(dto.getExecutorId()));

        if (dto.getId() != null && taskService.get(dto.getId()).isPresent())
            throw new TaskAlreadyExistsException(dto.getId());

        return ResponseEntity.ok().body(mapper.toDto(taskService.save(mapper.toEntity(dto), user, executor)));
    }

    @DeleteMapping("/{id}")
    @Secured("ADMIN")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Secured({"ADMIN", "USER"})
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid TaskDto dto, Principal principal) {
        User user = userService.getByEmail(principal.getName()).get();
        Task updatedTask = taskService.get(id).orElseThrow(() -> new TaskNotFoundException(id));
        User executor = userService.get(dto.getExecutorId()).orElseThrow(() -> new UserNotFoundException(dto.getExecutorId()));

        if (user.getRoles().contains(Role.ADMIN)) {
            updatedTask.setDescription(dto.getDescription());
            updatedTask.setStatus(Status.valueOf(dto.getStatus()));
            updatedTask.setPriority(Priority.valueOf(dto.getPriority()));
            updatedTask.setTitle(dto.getTitle());
            updatedTask.setExecutor(executor);
        } else if (updatedTask.getExecutor().getId().equals(user.getId()))
            updatedTask.setStatus(Status.valueOf(dto.getStatus()));

        return ResponseEntity.ok().body(mapper.toDto(taskService.save(updatedTask, user, executor)));
    }
}
