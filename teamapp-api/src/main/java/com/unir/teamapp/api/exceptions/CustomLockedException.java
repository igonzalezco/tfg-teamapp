package com.unir.teamapp.api.exceptions;

import java.io.Serial;

import org.springframework.security.authentication.LockedException;

import lombok.Getter;

@Getter
public class CustomLockedException extends LockedException {

    @Serial
    private static final long serialVersionUID = 2984232461686675666L;

    private final ErrorResponse errorResponse;


    public CustomLockedException(String message, ErrorResponse error) {
        super(message);
        this.errorResponse = error;
    }
    
    public CustomLockedException(String msg, Throwable e, ErrorResponse error) {
        super(msg, e);
        this.errorResponse = error;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
