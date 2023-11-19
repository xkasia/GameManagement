package com.game.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling custom exceptions in the application.
 * This class uses the Spring `ControllerAdvice` annotation to provide a centralized
 * way to handle specific exceptions and return appropriate HTTP responses.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),
                HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),
                HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameAlreadyExistsException.class)
    public ResponseEntity<Object> handleGameAlreadyExistsException(GameAlreadyExistsException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(),
                HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }
}
