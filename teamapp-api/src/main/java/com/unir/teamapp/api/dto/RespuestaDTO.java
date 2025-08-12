package com.unir.teamapp.api.dto;

import java.io.Serializable;

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
public class RespuestaDTO extends AuditableDTO implements Serializable { 

    private static final long serialVersionUID = 7669128324523510860L;

    private Integer id;

    private Boolean valor;

    private OpcionDTO opcion;

    private UsuarioVistaDTO usuario;

}
