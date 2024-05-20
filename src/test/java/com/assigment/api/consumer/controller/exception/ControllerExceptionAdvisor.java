package com.assigment.api.consumer.controller.exception;

import jakarta.servlet.ServletException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ControllerExceptionAdvisor {

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<String> illegalArgumentException(final IllegalArgumentException exception) {
        return new ResponseEntity<>(exception.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(value = IOException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> ioException(final IOException exception) {
        return new ResponseEntity<>(exception.getMessage(), INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(value = ServletException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> servletException(final ServletException exception) {
        return new ResponseEntity<>(exception.getMessage(), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<Exception> generalException(final Exception exception) {
        return new ResponseEntity<>(exception, INTERNAL_SERVER_ERROR);
    }
}
