package com.unir.teamapp.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
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
public class EquipoVistaDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 7669856982578510860L;

    private Integer id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String descripcion;
}
