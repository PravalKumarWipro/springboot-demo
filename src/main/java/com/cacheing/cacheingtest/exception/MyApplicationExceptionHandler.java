package com.cacheing.cacheingtest.exception;

import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MyApplicationExceptionHandler {
    @ExceptionHandler(CacheModuleException.class)
    public ResponseEntity<Response> toResponse(CacheModuleException exception) {
        Response response = new Response(AppConstants.FAILED);
        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}