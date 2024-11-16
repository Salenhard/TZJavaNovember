package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final UserService service;

    @Cacheable(value = "tasks", key = "#id")
    public Optional<Task> get(Long id) {
        return repository.findById(id);
    }


    public List<Task> getAll() {
        return repository.findAll();
    }

    @CachePut(value = "tasks", key = "#task.id")
    @Transactional
    public Task save(Task task) {
        return repository.save(task);
    }

    @CachePut(value = "tasks", key = "#task.id")
    @Transactional
    public Task save(Task task, Long authorId, Long executorId) {
        User author = service.get(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        User executor = service.get(executorId).orElseThrow(() -> new UserNotFoundException(executorId));
        task.setExecutor(executor);
        task.setAuthor(author);
        return repository.save(task);
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
