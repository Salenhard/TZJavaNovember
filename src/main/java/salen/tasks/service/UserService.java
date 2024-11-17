package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import salen.tasks.entity.User;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final AuthenticationManager authManger;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final JWTService jwtService;

    @Cacheable(value = "users", key = "#id")
    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public Optional<User> getByEmail(String email) {
        return repository.findByEmailAndIsActive(email, true);
    }

    public String verify(User user) {
        Authentication authentication =
                authManger.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (!authentication.isAuthenticated())
            throw new UserNotFoundException(user.getEmail());
        return jwtService.generateToken(user.getEmail());
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public List<User> getAll(Pageable pageable) {
        return repository.findAllByIsActive(true, pageable);
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
