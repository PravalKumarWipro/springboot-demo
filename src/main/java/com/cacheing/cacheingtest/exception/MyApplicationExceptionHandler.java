package com.cacheing.cacheingtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyApplicationExceptionHandler {
    @ExceptionHandler(UserModuleException.class)
    public ResponseEntity<String> toResponse(UserModuleException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}