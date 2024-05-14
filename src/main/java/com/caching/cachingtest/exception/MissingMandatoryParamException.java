package com.caching.cachingtest.exception;

/* This class is a custom exception used within a caching module or framework.
This exception is thrown when a key being inserted already exists in the cache */
public class MissingMandatoryParamException extends CacheModuleException {
    public MissingMandatoryParamException() {
    }

    public MissingMandatoryParamException(String message) {
        super(message);
    }

    public MissingMandatoryParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingMandatoryParamException(Throwable cause) {
        super(cause);
    }

    public MissingMandatoryParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
