package com.unir.teamapp.api.exceptions;

import java.io.Serial;
import java.util.Arrays;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2984668561686675666L;

    private final ErrorResponse error;

    public CustomException(final Exception e) {
        super(e);
        error = null;
    }

    public CustomException(final String message) {
        super(message);
        error = ErrorResponse.builder().details(Arrays.asList(message)).build();
    }

    public CustomException(final String message, final ErrorResponse error) {
        super(message);
        this.error = error;
    }

    public CustomException(final String message, final Exception e) {
        super(message, e);
        error = ErrorResponse.builder().details(Arrays.asList(message)).build();
    }

    public CustomException(final Throwable e, final ErrorResponse error) {
        super(e);
        this.error = error;
    }
}
