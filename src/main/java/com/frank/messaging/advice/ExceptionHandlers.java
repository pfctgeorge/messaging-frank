package com.frank.messaging.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) { // generic typc
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
}
