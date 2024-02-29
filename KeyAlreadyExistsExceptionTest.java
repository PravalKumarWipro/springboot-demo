package com.cacheing.cacheingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class KeyAlreadyExistsExceptionTest {
    @Test
    public void testDefaultConstructor() {
        KeyAlreadyExistsException exception = new KeyAlreadyExistsException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "User with email testuser@example.com already exists.";
        KeyAlreadyExistsException exception = new KeyAlreadyExistsException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to add user due to duplicate record.";
        Throwable cause = new RuntimeException("Database constraint violation.");
        KeyAlreadyExistsException exception = new KeyAlreadyExistsException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid user ID.");
        KeyAlreadyExistsException exception = new KeyAlreadyExistsException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testKeyAlreadyExistsExceptionConstructor(){
        String message = "Failed to add user due to duplicate record.";
        Throwable cause = new IllegalArgumentException("Invalid user ID.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        KeyAlreadyExistsException keyAlreadyExistsException=new KeyAlreadyExistsException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, keyAlreadyExistsException.getMessage());
        assertEquals(cause, keyAlreadyExistsException.getCause());
        assertEquals(enableSuppression, keyAlreadyExistsException.getSuppressed().length > 0);
        assertEquals(writableStackTrace, keyAlreadyExistsException.getStackTrace().length > 0);
    }
}
