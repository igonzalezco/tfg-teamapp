package com.unir.teamapp.api.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude
public class FiltersDTO implements Serializable {

  private int page;
  private int limit;
  private List<SortDTO> ordenaciones;
  private List<FilterDTO> filtros;
  private Integer identificador;
  private List<Integer> identificadores;

}
