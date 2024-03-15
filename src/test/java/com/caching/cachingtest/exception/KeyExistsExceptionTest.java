package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class KeyExistsExceptionTest {
    @Test
    public void testDefaultConstructor() {
        KeyExistsException exception = new KeyExistsException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Key already exists";
        KeyExistsException exception = new KeyExistsException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to add user due to duplicate record.";
        Throwable cause = new RuntimeException("Database constraint violation.");
        KeyExistsException exception = new KeyExistsException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid user ID.");
        KeyExistsException exception = new KeyExistsException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testKeyAlreadyExistsExceptionConstructor(){
        String message = "Failed to add user due to duplicate record.";
        Throwable cause = new IllegalArgumentException("Invalid user ID.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        KeyExistsException keyExistsException=new KeyExistsException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, keyExistsException.getMessage());
        assertEquals(cause, keyExistsException.getCause());
        assertEquals(enableSuppression, keyExistsException.getSuppressed().length > 0);
        assertEquals(writableStackTrace, keyExistsException.getStackTrace().length > 0);
    }
}
