package com.cacheing.cacheingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CacheMapModuleExceptionTest {
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
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to create user profile.";
        Throwable cause = new IllegalArgumentException("Invalid user data.");
        CacheModuleException exception = new CacheModuleException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalStateException("Database connection unavailable.");
        CacheModuleException exception = new CacheModuleException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }
}
