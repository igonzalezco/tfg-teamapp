package com.unir.teamapp.api.dto;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(Include.NON_NULL)
public class TokenUsuarioRolesDTO extends BaseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8956679464646874647L;

    private String token;
    // private UsuarioViewDTO usuario;
    // private EquiposVistaDTO equipos;

}
