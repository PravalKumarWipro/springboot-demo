package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when there is an issue updating the key to the cache as key was not present in the cache */
public class KeyNotExistsException extends CacheModuleException {
    public KeyNotExistsException() {
    }

    public KeyNotExistsException(String message) {
        super(message);
    }

    public KeyNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyNotExistsException(Throwable cause) {
        super(cause);
    }

    public KeyNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
