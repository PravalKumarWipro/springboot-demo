package com.cacheing.cacheingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UnleashAuthenticationExceptionTest {
    @Test
    public void testDefaultConstructor() {
        UnleashAuthenticationException exception = new UnleashAuthenticationException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Unable To Authenticate to Unleash Server!!!";
        UnleashAuthenticationException exception = new UnleashAuthenticationException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Authentication failed";
        Throwable cause = new RuntimeException("Root cause exception");
        UnleashAuthenticationException exception = new UnleashAuthenticationException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Authentication failed");
        UnleashAuthenticationException exception = new UnleashAuthenticationException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testKeyAlreadyExistsExceptionConstructor(){
        String message = "Unable To Authenticate to Unleash Server!!!";
        Throwable cause = new IllegalArgumentException("Root cause exception");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        UnleashAuthenticationException exception=new UnleashAuthenticationException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(enableSuppression, exception.getSuppressed().length > 0);
        assertEquals(writableStackTrace, exception.getStackTrace().length > 0);
    }
}
