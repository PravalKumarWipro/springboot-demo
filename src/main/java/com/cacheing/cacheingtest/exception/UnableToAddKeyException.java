package com.cacheing.cacheingtest.exception;

public class UnableToAddKeyException extends CacheModuleException {
    public UnableToAddKeyException() {
    }

    public UnableToAddKeyException(String message) {
        super(message);
    }

    public UnableToAddKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToAddKeyException(Throwable cause) {
        super(cause);
    }

    public UnableToAddKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
