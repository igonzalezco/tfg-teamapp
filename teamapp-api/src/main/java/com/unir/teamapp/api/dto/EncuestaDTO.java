package com.unir.teamapp.api.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@ToString(callSuper = true)
@AllArgsConstructor
public class EncuestaDTO extends AuditableDTO implements Serializable { 

    private static final long serialVersionUID = 7632702398782874860L;

    private Integer id;

    private String titulo;

    private String descripcion;

    private Boolean multiple;

    private EquipoDTO equipo;

    private List<OpcionDTO> opciones;

}
