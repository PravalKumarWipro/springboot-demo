package com.cacheing.cacheingtest.exception;

public class CacheModuleException extends RuntimeException {

    public CacheModuleException() {
    }

    public CacheModuleException(String message) {
        super(message);
    }

    public CacheModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheModuleException(Throwable cause) {
        super(cause);
    }

    public CacheModuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
