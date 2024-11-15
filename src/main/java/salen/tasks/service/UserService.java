package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salen.tasks.entity.User;
import salen.tasks.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public Optional<User> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public User save(User user) {
        return repository.save(user);
    }
}
