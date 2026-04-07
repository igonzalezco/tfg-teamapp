package com.unir.teamapp.api.exceptions;

import java.io.Serial;
import java.util.Arrays;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2984668561686675666L;

    private final ErrorResponse error;

    private final HttpStatus status;

    public CustomException(final Exception e) {
        super(e);
        error = null;
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(final String message) {
        super(message);
        error = ErrorResponse.builder().details(Arrays.asList(message)).build();
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(final String message, final ErrorResponse error) {
        super(message);
        this.error = error;
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(final String message, HttpStatus status) {
        super(message);
        error = ErrorResponse.builder().details(Arrays.asList(message)).build();
        this.status = status;
    }

    public CustomException(final String message, final Exception e) {
        super(message, e);
        error = ErrorResponse.builder().details(Arrays.asList(message)).build();
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(final Throwable e, final ErrorResponse error) {
        super(e);
        this.error = error;
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CustomException(final String message, final ErrorResponse error, HttpStatus status) {
        super(message);
        this.error = error;
        this.status = status;
    }

    public CustomException(final String message, final Exception e, HttpStatus status) {
        super(message, e);
        error = ErrorResponse.builder().details(Arrays.asList(message)).build();
        this.status = status;
    }
}
