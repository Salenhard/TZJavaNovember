package salen.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import salen.tasks.entity.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
