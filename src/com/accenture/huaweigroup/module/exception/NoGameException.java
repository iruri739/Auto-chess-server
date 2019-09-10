package com.accenture.huaweigroup.module.exception;

public class NoGameException extends Exception {
    public NoGameException(String message) {
        super(message);
    }

    public NoGameException(String message, Throwable cause) {
        super(message, cause);
    }
}
