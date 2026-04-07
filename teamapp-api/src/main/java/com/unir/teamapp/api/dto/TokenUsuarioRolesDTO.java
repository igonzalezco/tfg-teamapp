package com.unir.teamapp.api.dto;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@ToString
@Builder
@AllArgsConstructor
public class TokenUsuarioRolesDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8956679464646874647L;

    private String token;

    private UsuarioVistaDTO usuario;
}
