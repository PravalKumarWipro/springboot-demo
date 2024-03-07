package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CacheModuleExceptionTest {
    @Test
    public void testDefaultConstructor() {
        CacheModuleException exception = new CacheModuleException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof RuntimeException);
    }

    @Test
    public void testMessageConstructor() {
        String message = "An error occurred in the user module.";
        CacheModuleException exception = new CacheModuleException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to create user profile.";
        Throwable cause = new IllegalArgumentException("Invalid user data.");
        CacheModuleException exception = new CacheModuleException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalStateException("Database connection unavailable.");
        CacheModuleException exception = new CacheModuleException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCacheModuleExceptionConstructor() {
        String message = "Failed to create user profile.";
        Throwable cause = new IllegalArgumentException("Invalid user data.");
        boolean enableSuppression = false;
        boolean writableStackTrace = false;
        CacheModuleException exception = new CacheModuleException(message, cause, enableSuppression, writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(enableSuppression, exception.getSuppressed().length > 0);
        assertEquals(writableStackTrace, exception.getStackTrace().length > 0);
    }
}
