package salen.tasks.exception;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(Long id) {
        super("Task with id:" + id + " already exists");
    }
}
