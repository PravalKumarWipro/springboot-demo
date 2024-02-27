package com.cacheing.cacheingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CacheMapNotFoundExceptionTest {
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
}
