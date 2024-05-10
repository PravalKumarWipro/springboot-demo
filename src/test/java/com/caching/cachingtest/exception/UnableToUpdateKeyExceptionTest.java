package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UnableToUpdateKeyExceptionTest {
    @Test
    public void testDefaultConstructor() {
        UnableToUpdateKeyException exception = new UnableToUpdateKeyException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Key already exists";
        UnableToUpdateKeyException exception = new UnableToUpdateKeyException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to add user due to duplicate record.";
        Throwable cause = new RuntimeException("Database constraint violation.");
        UnableToUpdateKeyException exception = new UnableToUpdateKeyException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid user ID.");
        UnableToUpdateKeyException exception = new UnableToUpdateKeyException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testUnableToUpdateKeyExceptionConstructor(){
        String message = "Failed to add user due to duplicate record.";
        Throwable cause = new IllegalArgumentException("Invalid user ID.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        UnableToUpdateKeyException unableToUpdateKeyException =new UnableToUpdateKeyException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, unableToUpdateKeyException.getMessage());
        assertEquals(cause, unableToUpdateKeyException.getCause());
        assertEquals(enableSuppression, unableToUpdateKeyException.getSuppressed().length > 0);
        assertEquals(writableStackTrace, unableToUpdateKeyException.getStackTrace().length > 0);
    }
}
