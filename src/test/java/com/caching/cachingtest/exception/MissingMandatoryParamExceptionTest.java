package com.caching.cachingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MissingMandatoryParamExceptionTest {
    @Test
    public void testDefaultConstructor() {
        MissingMandatoryParamException exception = new MissingMandatoryParamException();
        Assert.assertNotNull(exception);
        Assert.assertTrue(exception instanceof CacheModuleException);
    }
    @Test
    public void testMessageConstructor() {
        String message = "Missing mandatory parameters";
        MissingMandatoryParamException exception = new MissingMandatoryParamException(message);
        Assert.assertEquals(message, exception.getMessage());
    }

    @Test
    public void testMessageCauseConstructor() {
        String message = "Failed to add user due to missing mandatory params.";
        Throwable cause = new RuntimeException("Database constraint violation.");
        MissingMandatoryParamException exception = new MissingMandatoryParamException(message, cause);
        Assert.assertEquals(message, exception.getMessage());
        Assert.assertEquals(cause, exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new IllegalArgumentException("Invalid key or value.");
        MissingMandatoryParamException exception = new MissingMandatoryParamException(cause);
        Assert.assertEquals(cause, exception.getCause());
    }

    @Test
    public void testMissingMandatoryParamExceptionConstructor(){
        String message = "Failed to add user due to missing mandatory params.";
        Throwable cause = new IllegalArgumentException("Invalid key or value.");
        boolean enableSuppression = false;
        boolean writableStackTrace=false;
        MissingMandatoryParamException missingMandatoryParamException=new MissingMandatoryParamException(message,cause,enableSuppression,writableStackTrace);
        assertEquals(message, missingMandatoryParamException.getMessage());
        assertEquals(cause, missingMandatoryParamException.getCause());
        assertEquals(enableSuppression, missingMandatoryParamException.getSuppressed().length > 0);
        assertEquals(writableStackTrace, missingMandatoryParamException.getStackTrace().length > 0);
    }
}
