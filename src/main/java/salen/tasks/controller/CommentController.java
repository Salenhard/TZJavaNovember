package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.Comment;
import salen.tasks.entity.dto.CommentDto;
import salen.tasks.entity.mapper.CommentMapper;
import salen.tasks.exception.CommentAlreadyExistsException;
import salen.tasks.exception.CommentNotFoundException;
import salen.tasks.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;
    private final CommentMapper mapper;

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable Long id) {
        //todo maybe by taskId?
        return service.get(id).map(mapper::toDto).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @GetMapping
    public List<CommentDto> getAll() {
        //todo by taskId
        return service.getAll().stream().map(mapper::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable Long taskId, @RequestBody @Valid CommentDto dto) {
        if (dto.getId() != null && service.get(dto.getId()).isPresent())
            throw new CommentAlreadyExistsException(dto.getId());
        Long loggedUserId = 1L; //todo get from session jwt token kekw
        return ResponseEntity.ok().body(mapper.toDto(service.save(mapper.toEntity(dto), loggedUserId, dto.getTaskId())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(Long id) {
        Long loggedUserId = 1L; //todo get from session jwt token kekw
        service.delete(id, loggedUserId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable Long id, @RequestBody @Valid CommentDto dto) {
        Comment updatedComment = service.get(id).map((comment) -> {
                    comment.setValue(comment.getValue());
                    return comment;
                }
        ).orElseThrow(() -> new CommentNotFoundException(id));

        return mapper.toDto(service.save(updatedComment, dto.getAuthorId(), dto.getTaskId()));
    }
}
