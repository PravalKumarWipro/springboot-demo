package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class KeyNotExistsExceptionTest {
    @Test
    public void testDefaultConstructor() {
        KeyNotExistsException exception = new KeyNotExistsException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Key does not exists";
        KeyNotExistsException exception = new KeyNotExistsException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Key not found";
        Throwable cause = new RuntimeException("Database constraint violation.");
        KeyNotExistsException exception = new KeyNotExistsException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid key.");
        KeyNotExistsException exception = new KeyNotExistsException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testKeyNotExistsExceptionConstructor(){
        String message = "Key not found";
        Throwable cause = new IllegalArgumentException("Invalid key");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        KeyNotExistsException keyNotExistsException=new KeyNotExistsException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, keyNotExistsException.getMessage());
        assertEquals(cause, keyNotExistsException.getCause());
        assertEquals(enableSuppression, keyNotExistsException.getSuppressed().length > 0);
        assertEquals(writableStackTrace, keyNotExistsException.getStackTrace().length > 0);
    }
}
