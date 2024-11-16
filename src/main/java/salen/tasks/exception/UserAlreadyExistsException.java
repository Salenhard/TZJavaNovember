package salen.tasks.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(Long id) {
        super("User with id: " + id + " already exists");
    }

    public UserAlreadyExistsException(String email) {
        super("User with email: " + email + " already exists");
    }
}
