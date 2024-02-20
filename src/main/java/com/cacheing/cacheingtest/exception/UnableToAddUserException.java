package com.cacheing.cacheingtest.exception;

public class UnableToAddUserException extends UserModuleException {
    public UnableToAddUserException() {
    }

    public UnableToAddUserException(String message) {
        super(message);
    }

    public UnableToAddUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToAddUserException(Throwable cause) {
        super(cause);
    }

    public UnableToAddUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
