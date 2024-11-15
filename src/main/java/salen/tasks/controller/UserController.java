package salen.tasks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import salen.tasks.entity.Role;
import salen.tasks.entity.User;
import salen.tasks.entity.dto.UserDto;
import salen.tasks.entity.mapper.UserMapper;
import salen.tasks.exception.UserAlreadyExistsException;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return mapper.toDto(service.get(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll().stream().map(mapper::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid UserDto dto) {
        if (dto.getId() != null && service.get(dto.getId()).isPresent())
            throw new UserAlreadyExistsException(dto.getId());
        return ResponseEntity.ok().body(mapper.toDto(service.save(mapper.toEntity(dto))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid UserDto dto) {
        User updatedUser = service.get(id).map((newUser) -> {
                    newUser.setEmail(dto.getEmail());
                    newUser.setPassword(dto.getPassword());
                    newUser.setRoles(dto.getRoles().stream()
                            .map(Role::valueOf)
                            .collect(Collectors.toSet()));
                    return newUser;
                }
        ).orElseThrow(() -> new UserNotFoundException(id));
        return ResponseEntity.ok().body(mapper.toDto(service.save(updatedUser)));
    }
}