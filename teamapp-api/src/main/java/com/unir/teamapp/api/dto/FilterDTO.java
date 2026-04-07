package com.unir.teamapp.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.criteria.Predicate.BooleanOperator;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterDTO {

  private String campo;
  private String valor;
  private String tipo;
  private String expression;
  private String comparacion;
  private String joinType;
  private String entityJoinName;
  private BooleanOperator operador;
  private Boolean prefiltrado;
  private String fieldNameCompareColumn;

}
