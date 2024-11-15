package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.Priority;
import salen.tasks.entity.Status;
import salen.tasks.entity.Task;
import salen.tasks.entity.dto.TaskDto;
import salen.tasks.entity.mapper.TaskMapper;
import salen.tasks.exception.TaskAlreadyExistsException;
import salen.tasks.exception.TaskNotFoundException;
import salen.tasks.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    private final TaskMapper mapper;

    @GetMapping("/{id}")
    public TaskDto get(@PathVariable Long id) {
        return service.get(id).map(mapper::toDto).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @GetMapping
    public List<TaskDto> getAll() {
        return service.getAll().stream().map(mapper::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid TaskDto dto) {
        if (dto.getId() != null && service.get(dto.getId()).isPresent())
            throw new TaskAlreadyExistsException(dto.getId());
        return ResponseEntity.ok()
                .body(mapper.toDto(service.save(mapper.toEntity(dto), dto.getAuthorId(), dto.getExecutorId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid TaskDto dto) {
        Task updatedTask = service.get(id).map(newTask ->
        {
            newTask.setDescription(dto.getDescription());
            newTask.setStatus(Status.valueOf(dto.getStatus()));
            newTask.setPriority(Priority.valueOf(dto.getPriority()));
            newTask.setTitle(dto.getTitle());
            return newTask;
        }).orElseThrow(() -> new TaskNotFoundException(id));
        return ResponseEntity.ok()
                .body(mapper.toDto(service.save(updatedTask, dto.getAuthorId(), dto.getExecutorId())));
    }
}
