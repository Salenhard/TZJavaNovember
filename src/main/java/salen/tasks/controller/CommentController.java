package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.Comment;
import salen.tasks.entity.dto.CommentDto;
import salen.tasks.entity.mapper.CommentMapper;
import salen.tasks.exception.CommentAlreadyExistsException;
import salen.tasks.exception.CommentNotFoundException;
import salen.tasks.service.CommentService;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;
    private final CommentMapper mapper;

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable Long taskId, @PathVariable Long id) {
        return service.get(id, taskId).map(mapper::toDto).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @GetMapping
    public Page<CommentDto> getAll(@PathVariable Long taskId, @RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size) {
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, page.orElse(10));
        return service.getAll(taskId, pageable).map(mapper::toDto);
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable Long taskId, @RequestBody @Valid CommentDto dto) {
        if (dto.getId() != null && service.get(dto.getId(), taskId).isPresent())
            throw new CommentAlreadyExistsException(dto.getId());
        Long loggedUserId = 1L; //todo get from session
        return ResponseEntity.ok().body(mapper.toDto(service.save(mapper.toEntity(dto), loggedUserId, taskId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long taskId, @PathVariable Long id) {
        Long loggedUserId = 1L; //todo get from session
        service.delete(id, loggedUserId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable Long taskId, @PathVariable Long id, @RequestBody @Valid CommentDto dto) {
        Comment updatedComment = service.get(id, taskId).map((comment) -> {
                    comment.setValue(dto.getValue());
                    return comment;
                }
        ).orElseThrow(() -> new CommentNotFoundException(id));
        Long userId = 1L;
        return mapper.toDto(service.save(updatedComment, userId, taskId));
    }
}
