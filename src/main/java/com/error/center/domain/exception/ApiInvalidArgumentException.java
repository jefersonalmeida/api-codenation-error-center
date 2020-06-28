package com.error.center.domain.exception;

public class ApiInvalidArgumentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiInvalidArgumentException(String message) {
        super(message);
    }
}
