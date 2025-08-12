package com.unir.teamapp.api.dto;

import java.io.Serializable;
import java.util.List;

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
public class UsuarioVistaDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 7673535787829804860L;

    private String email;

    private RolDTO rol;
    
    private List<UsuarioEquipoDTO> usuarioEquipos;

}
