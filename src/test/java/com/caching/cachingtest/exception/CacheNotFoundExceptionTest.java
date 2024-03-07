package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
@RunWith(MockitoJUnitRunner.class)
public class CacheNotFoundExceptionTest {
    @Test
    public void testDefaultConstructor() {
        CacheNotFoundException exception = new CacheNotFoundException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "User with ID 123 not found.";
        CacheNotFoundException exception = new CacheNotFoundException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to retrieve user data.";
        Throwable cause = new IllegalStateException("Database connection lost.");
        CacheNotFoundException exception = new CacheNotFoundException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid user name.");
        CacheNotFoundException exception = new CacheNotFoundException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCacheNotFoundExceptionConstructor(){
        String message = "Failed to retrieve user data.";
        Throwable cause = new IllegalStateException("Database connection lost.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        CacheNotFoundException exception=new CacheNotFoundException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(enableSuppression, exception.getSuppressed().length > 0);
        assertEquals(writableStackTrace, exception.getStackTrace().length > 0);
    }
}
