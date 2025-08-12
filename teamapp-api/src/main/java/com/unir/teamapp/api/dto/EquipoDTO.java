package com.unir.teamapp.api.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
public class EquipoDTO extends AuditableDTO implements Serializable { 

    private static final long serialVersionUID = 7669856982578510860L;

    private Integer id;

    private String nombre;

    private String descripcion;

    private List<JugadorDTO> jugadores;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<CuotaDTO> cuotas;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<NoticiaDTO> noticias;

    @OneToMany(cascade= CascadeType.ALL, mappedBy = "equipo", fetch = FetchType.LAZY)
    private List<EncuestaDTO> encuestas;

}
