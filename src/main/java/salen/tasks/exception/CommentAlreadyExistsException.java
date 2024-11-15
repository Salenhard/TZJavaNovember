package salen.tasks.exception;

public class CommentAlreadyExistsException extends RuntimeException {
    public CommentAlreadyExistsException(Long id) {
        super("Comment with id: " + id + " already exists");
    }
}
