package com.unir.teamapp.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(Include.NON_NULL)
@ToString
@Builder
@AllArgsConstructor
public class LoginRequestDTO implements Serializable {

    private static final long serialVersionUID = -6982561564871354753L;

    @Valid
    @NotBlank
    @Email
    @Size(min = 0, max = 100)
    private String username;

    @Valid
    @NotBlank
    @Size(min = 0, max = 255)
    private String password;

}
