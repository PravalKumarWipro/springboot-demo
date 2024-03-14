package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class InvalidTTLExceptionTest {
    @Test
    public void testDefaultConstructor() {
        InvalidTTLException exception = new InvalidTTLException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof InvalidTTLException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Invalid TTL - TTL should be > 0";
        InvalidTTLException exception = new InvalidTTLException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Invalid TTL - TTL should be > 0";
        Throwable cause = new IllegalStateException("Database connection lost.");
        InvalidTTLException exception = new InvalidTTLException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid TTL - TTL should be > 0");
        InvalidTTLException exception = new InvalidTTLException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testInvalidTTLExceptionConstructor(){
        String message = "Invalid TTL - TTL should be > 0";
        Throwable cause = new IllegalStateException("Database connection lost.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        InvalidTTLException exception=new InvalidTTLException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(enableSuppression, exception.getSuppressed().length > 0);
        assertEquals(writableStackTrace, exception.getStackTrace().length > 0);
    }
}
