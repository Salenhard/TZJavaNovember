package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.dto.CommentDto;
import salen.tasks.entity.mapper.CommentMapper;
import salen.tasks.exception.CommentNotFoundException;
import salen.tasks.service.CommentService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper mapper;

    @GetMapping("/{id}")
    public CommentDto get(@PathVariable Long taskId,
                          @PathVariable Long id) {
        return commentService.get(id, taskId).map(mapper::toDto).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @GetMapping
    public Page<CommentDto> getAll(@PathVariable Long taskId,
                                   @RequestParam(required = false) Optional<Integer> page,
                                   @RequestParam(required = false) Optional<Integer> size,
                                   @RequestParam(required = false) String author) {
        Pageable pageable = PageRequest.of(page.orElse(1) - 1, size.orElse(10));
        return commentService.getAll(taskId, author, pageable).map(mapper::toDto);
    }

    @PostMapping
    public ResponseEntity<?> save(@PathVariable Long taskId,
                                  @RequestBody @Valid CommentDto dto,
                                  Principal principal) {
        return ResponseEntity.ok().body(mapper.toDto(commentService.save(mapper.toEntity(dto), principal.getName(), taskId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long taskId,
                                    @PathVariable Long id,
                                    Principal principal) {
        commentService.delete(id, principal.getName(), taskId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public CommentDto update(@PathVariable Long taskId, @PathVariable Long id, @RequestBody @Valid CommentDto dto, Principal principal) {
        return mapper.toDto(commentService.update(id, mapper.toEntity(dto), principal.getName(), taskId));
    }
}
