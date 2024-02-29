package com.cacheing.cacheingtest.exception;


import com.cacheing.cacheingtest.AppConstants;
import com.cacheing.cacheingtest.model.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class MyApplicationExceptionHandlerTest {

    private final MyApplicationExceptionHandler exceptionHandler = new MyApplicationExceptionHandler();
    @Test
    public void testToResponse_withCacheModuleException() {
        String exceptionMessage = "Cache model error";
        CacheModuleException exception = new CacheModuleException(exceptionMessage);
        MyApplicationExceptionHandler exceptionHandler = new MyApplicationExceptionHandler();
        ResponseEntity<Response> response = exceptionHandler.toResponse(exception);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assert.assertEquals(AppConstants.FAILED, response.getBody().getStatus());
        Assert.assertEquals(exceptionMessage, response.getBody().getMessage());
    }
}
