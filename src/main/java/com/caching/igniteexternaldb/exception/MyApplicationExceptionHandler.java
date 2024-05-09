package com.caching.igniteexternaldb.exception;

import com.caching.igniteexternaldb.AppConstants;
import com.caching.igniteexternaldb.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* Global exception handler to handle all exceptions while dealing with CacheModuleException class*/
@ControllerAdvice
public class MyApplicationExceptionHandler {
    @ExceptionHandler(CacheModuleException.class)
    public ResponseEntity<Response> toResponse(CacheModuleException exception) {
        Response response = new Response(AppConstants.FAILED);
        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}