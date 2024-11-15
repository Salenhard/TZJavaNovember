package salen.tasks.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salen.tasks.entity.Task;
import salen.tasks.entity.User;
import salen.tasks.entity.dto.TaskDto;
import salen.tasks.entity.mapper.TaskMapper;
import salen.tasks.exception.UserNotFoundException;
import salen.tasks.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository repository;
    private final UserService service;

    public Optional<Task> get(Long id) {
        return repository.findById(id);
    }


    public List<Task> getAll() {
        return repository.findAll();
    }


    public Task save(Task task) {
        return repository.save(task);
    }

    public Task save(Task task, Long authorId, Long executorId) {
        User author = service.get(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        User executor = service.get(executorId).orElseThrow(() -> new UserNotFoundException(executorId));
        task.setExecutor(executor);
        task.setAuthor(author);
        return repository.save(task);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
