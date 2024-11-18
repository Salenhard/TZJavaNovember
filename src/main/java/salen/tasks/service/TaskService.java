package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.repository.TaskRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static salen.tasks.util.TaskSpecification.*;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;

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
    public Task save(Task task, User author, User executor) {
        task.setExecutor(executor);
        task.setAuthor(author);
        return repository.save(task);
    }

    @Transactional
    @CacheEvict(value = "tasks", key = "#id")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Task> getAll(String title, String status, String priority, String authorEmail, String executorEmail, Pageable pageable) {
        Specification<Task> filters = Specification.where(Objects.isNull(authorEmail) ? null : likeAuthorEmail(authorEmail))
                .and(Objects.isNull(title) ? null : likeTitle(title))
                .and(Objects.isNull(executorEmail) ? null : likeAuthorEmail(authorEmail))
                .and(Objects.isNull(status) ? null : equalStatus(status))
                .and(Objects.isNull(priority) ? null : equalPriority(priority));
        return repository.findAll(filters, pageable);
    }
}
