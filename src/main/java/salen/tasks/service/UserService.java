package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import salen.tasks.entity.User;
import salen.tasks.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Cacheable(value = "users", key = "#id")
    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public Page<User> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<User> getByEmail(String email) {
        return repository.findByEmailAndIsActive(email, true);
    }

    public List<User> getAll() {
        return repository.findAllByIsActive(true);
    }

    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        Optional<User> byId = repository.findById(id);
        byId.ifPresent(user -> {
            user.setActive(false);
            repository.save(user);
        });
    }

    @CachePut(value = "users", key = "#user.id")
    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

}
