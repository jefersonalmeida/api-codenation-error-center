package com.error.center.domain.exception;

public class ApiModelNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiModelNotFoundException(String message) {
        super(message);
    }
}
