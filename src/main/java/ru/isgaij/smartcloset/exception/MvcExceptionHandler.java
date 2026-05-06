package ru.isgaij.smartcloset.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MvcExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        log.error("Internal server error (MVC): {}", e.getMessage(), e);
        model.addAttribute("message", e.getMessage());
        return "error/500";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException e, Model model) {
        log.warn("Resource not found (MVC): {}", e.getMessage());
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }
}