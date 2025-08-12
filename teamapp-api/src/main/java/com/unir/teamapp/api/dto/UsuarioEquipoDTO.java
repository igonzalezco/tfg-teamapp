package com.unir.teamapp.api.dto;

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
public class UsuarioEquipoDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 7676039887829874860L;

    private Integer id;

    private UsuarioVistaDTO usuario;

    private EquipoDTO equipo;

    private PermisoDTO permiso;

}
