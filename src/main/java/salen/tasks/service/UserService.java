package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salen.tasks.entity.User;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.repository.UserRepository;

import java.util.Optional;

import static salen.tasks.util.UserSpecification.isActive;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final AuthenticationManager authManger;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
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

    public Page<User> getAll(Pageable pageable) {
        boolean isActive = true;
        Specification<User> filters = Specification.where(isActive(isActive));
        return repository.findAll(filters, pageable);
    }

    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void delete(Long id) {
        repository.findById(id).ifPresent(user -> {
            user.setActive(false);
            repository.save(user);
        });
    }

    @CachePut(value = "users", key = "#user.id")
    @Transactional
    public User save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repository.save(user);
    }

}
