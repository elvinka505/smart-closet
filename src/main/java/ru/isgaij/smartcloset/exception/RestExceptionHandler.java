package ru.isgaij.smartcloset.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.isgaij.smartcloset.dto.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {
    public static final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException e) {
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now().toString());
        log.error("Internal server error: {}", e.getMessage(), e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException e) {
        ApiError apiError = new ApiError();
        apiError.setMessage(e.getMessage());
        apiError.setTimestamp(LocalDateTime.now().toString());
        log.warn("Resource not found: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiError);
    }

}
