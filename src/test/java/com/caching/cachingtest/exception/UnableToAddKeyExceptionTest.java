package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UnableToAddKeyExceptionTest {
    @Test
    public void testDefaultConstructor() {
        UnableToAddKeyException exception = new UnableToAddKeyException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Failed to add user.";
        UnableToAddKeyException exception = new UnableToAddKeyException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Unable to add user due to database error.";
        Throwable cause = new RuntimeException("Database connection failed.");
        UnableToAddKeyException exception = new UnableToAddKeyException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid user data.");
        UnableToAddKeyException exception = new UnableToAddKeyException(cause);
        Assert.assertEquals(cause, exception.getCause());

    }

    @Test
    public void testUnableToAddKeyExceptionConstructor(){
        String message = "Failed to retrieve user data.";
        Throwable cause = new IllegalStateException("Database connection lost.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        UnableToAddKeyException exception=new UnableToAddKeyException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(enableSuppression, exception.getSuppressed().length > 0);
        assertEquals(writableStackTrace, exception.getStackTrace().length > 0);
    }
}
