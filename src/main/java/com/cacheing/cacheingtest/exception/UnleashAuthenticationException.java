package com.cacheing.cacheingtest.exception;

public class UnleashAuthenticationException extends CacheModuleException {
    public UnleashAuthenticationException() {
    }

    public UnleashAuthenticationException(String message) {
        super(message);
    }

    public UnleashAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnleashAuthenticationException(Throwable cause) {
        super(cause);
    }

    public UnleashAuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
