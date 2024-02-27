package com.cacheing.cacheingtest.exception;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UnableToAddCacheMapExceptionTest {
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
}
