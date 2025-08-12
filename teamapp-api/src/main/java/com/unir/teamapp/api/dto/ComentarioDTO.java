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
public class ComentarioDTO extends AuditableDTO implements Serializable {

    private static final long serialVersionUID = 7636574157829874860L;

    private Integer id;

    private String texto;

    private NoticiaDTO noticia;

    private UsuarioVistaDTO usuario;

}
