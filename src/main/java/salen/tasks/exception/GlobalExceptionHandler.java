package salen.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({CommentNotFoundException.class, TaskNotFoundException.class, UserNotFoundException.class})
    String handleEntityNotFoundExceptions(RuntimeException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({CommentAlreadyExistsException.class, TaskAlreadyExistsException.class, UserAlreadyExistsException.class})
    String handleEntityAlreadyExistsExceptions(RuntimeException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    String handleRuntimeExceptions(RuntimeException ex) {
        return ex.getMessage();
    }

}
