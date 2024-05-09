package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when there is an issue while updating the key to the cache */
public class UnableToUpdateKeyException extends CacheModuleException {
    public UnableToUpdateKeyException() {
    }

    public UnableToUpdateKeyException(String message) {
        super(message);
    }

    public UnableToUpdateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToUpdateKeyException(Throwable cause) {
        super(cause);
    }

    public UnableToUpdateKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
