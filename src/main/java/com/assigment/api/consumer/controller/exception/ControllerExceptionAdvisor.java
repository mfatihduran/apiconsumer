package com.assigment.api.consumer.controller.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static reactor.core.publisher.Mono.just;

@ControllerAdvice
public class ControllerExceptionAdvisor {

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public Mono<ResponseEntity<String>> illegalArgumentException(final IllegalArgumentException exception) {
        return just(new ResponseEntity<>(exception.getMessage(), BAD_REQUEST));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Mono<ResponseEntity<Exception>> generalException(final Exception exception) {
        return just(new ResponseEntity<>(exception, INTERNAL_SERVER_ERROR));
    }
}
