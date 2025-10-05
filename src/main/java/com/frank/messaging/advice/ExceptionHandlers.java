package com.frank.messaging.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) { // generic typc
        log.warn("Encountered exception: {}", exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(exception.getMessage());

    }
}
