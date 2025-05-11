package com.unir.teamapp.api.exceptions;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ErrorResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -2116879323874668972L;

    private String message;

    private List<String> details;

    private String errorCode;

}
