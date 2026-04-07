package com.unir.teamapp.persist.util.filter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.unir.teamapp.api.dto.FilterDTO;
import com.unir.teamapp.api.dto.FiltersDTO;
import com.unir.teamapp.api.dto.SortDTO;
import com.unir.teamapp.api.exceptions.CustomException;
import com.unir.teamapp.persist.entity.BaseEntity;
import com.unir.teamapp.persist.util.filter.expression.ExpressionType;
import com.unir.teamapp.persist.util.filter.expression.ParamExpression;
import com.unir.teamapp.persist.util.filter.expression.SimpleExpression;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate.BooleanOperator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterManagement<T extends BaseEntity> {

  private static final String CAMPO_ID = "id";

  private PageRequest pageable;
  private String sortField;
  private Sort.Direction sortDir = Sort.Direction.ASC;
  private SpecificationFilter<T> specificationFilter;
  private BooleanOperator operador = BooleanOperator.AND;
  private FiltersDTO filtersDTO;
  private Sort sort;

  public FilterManagement(final FiltersDTO filtersDTO, final String defaultSortField,
      final Sort.Direction defaultSortDir) {
    this.filtersDTO = filtersDTO;
    sortField = defaultSortField;
    sortDir = defaultSortDir != null ? defaultSortDir : sortDir;
    specificationFilter = new SpecificationFilterBean<>();

    loadSortConfig();
    loadPaginationConfig();

    try {
      loadFilteringFields();
    } catch (final Exception e) {
      throw new CustomException("Error al procesar los filtros", e);
    }
  }

  public void loadSortConfig() {
    if (filtersDTO == null) {
      sort = null;
      return;
    }

    final List<SortDTO> ordenaciones = filtersDTO.getOrdenaciones();

    if (CollectionUtils.isNotEmpty(ordenaciones)) {
      sort = ordenaciones.stream().map(item -> Sort.by(Sort.Direction.fromString(item.getOrden()), item.getCampo()))
          .reduce(Sort::and).orElse(null);

      if (ordenaciones.size() == 1 && FilterManagement.CAMPO_ID.equalsIgnoreCase(ordenaciones.get(0).getCampo())) {
        sort = (sort == null) ? Sort.by(sortDir, sortField) : sort.and(Sort.by(sortDir, sortField));
      }
    } else {
      sort = Sort.by(sortDir, sortField);
    }
  }

  public void loadPaginationConfig() {
    if (filtersDTO == null) {
      return;
    }

    if (CollectionUtils.isNotEmpty(filtersDTO.getOrdenaciones())) {
      pageable = PageRequest.of(filtersDTO.getPage(), filtersDTO.getLimit(), sort);
    } else if (sortField == null) {
      pageable = PageRequest.of(filtersDTO.getPage(), filtersDTO.getLimit());
    } else {
      pageable = PageRequest.of(filtersDTO.getPage(), filtersDTO.getLimit(), sortDir, sortField);
    }
  }

  public void loadFilteringFields() throws Exception {
    if (filtersDTO == null || CollectionUtils.isEmpty(filtersDTO.getFiltros())) {
      return;
    }

    final List<FilterDTO> filtrosValidos = filtersDTO.getFiltros().stream()
        .filter(f -> f != null && StringUtils.isNotEmpty(f.getTipo())).toList();

    for (final FilterDTO filtro : filtrosValidos) {
      final ExpressionType expressionType = getExpressionType(filtro);
      final SimpleExpression expression = buildExpression(filtro, expressionType);

      if (expression != null && !StringUtils.isBlank(filtro.getValor())) {
        operador = filtro.getOperador() != null ? filtro.getOperador() : operador;
        specificationFilter.addColumnExpression(filtro.getCampo(), operador, expression);
      }
    }
  }

  private SimpleExpression buildExpression(final FilterDTO filtro, final ExpressionType expressionType)
      throws ParseException {
    final boolean hasValue = StringUtils.isNotEmpty(filtro.getValor());
    final boolean hasJoin = StringUtils.isNotEmpty(filtro.getJoinType())
        && StringUtils.isNotEmpty(filtro.getEntityJoinName());

    final ParamExpression params = new ParamExpression(filtro.getCampo(), hasValue ? returnRealValue(filtro) : null,
        expressionType, filtro.getFieldNameCompareColumn(), Boolean.TRUE);

    if (hasJoin) {
      return new SimpleExpression(filtro.getEntityJoinName(), JoinType.valueOf(filtro.getJoinType()), List.of(params));
    } else {
      return SimpleExpression.of(params);
    }

  }

  private ExpressionType getExpressionType(final FilterDTO filtro) {
    final String tipo = filtro.getTipo();

    return switch (tipo) {
      case "string" -> mapStringExpression(filtro.getExpression());
      case "integer", "long", "double", "float" -> mapNumericComparison(filtro.getExpression());
      case "list" -> ExpressionType.IN;
      case "boolean" -> ExpressionType.EQUALS;
      case "date" -> mapDateComparison(filtro.getComparacion());
      case "dateTime" -> ExpressionType.BETWEEN;
      default -> null;
    };
  }

  private ExpressionType mapStringExpression(final String expresion) {
    return switch (expresion) {
      case "CONTAINS" -> ExpressionType.CONTAINS;
      case "EQUALS" -> ExpressionType.EQUALS;
      case "IS_NULL" -> ExpressionType.IS_NULL;
      case "IS_NOT_NULL" -> ExpressionType.IS_NOT_NULL;
      case "NOT_EQUALS" -> ExpressionType.NOT_EQUALS;
      default -> ExpressionType.EQUALS;
    };
  }

  private ExpressionType mapNumericComparison(final String comparacion) {
    return switch (comparacion) {
      case "EQUALS" -> ExpressionType.EQUALS;
      case "LT" -> ExpressionType.LESS_THAN;
      case "LTE" -> ExpressionType.LESS_OR_EQUALS;
      case "GT" -> ExpressionType.GREATER_THAN;
      case "GTE" -> ExpressionType.GREATER_OR_EQUALS;
      case "NOT_EQUALS" -> ExpressionType.NOT_EQUALS;
      default -> ExpressionType.EQUALS;
    };
  }

  private ExpressionType mapDateComparison(final String comparacion) {
    return switch (comparacion) {
      case "on" -> ExpressionType.EQUALS;
      case "after" -> ExpressionType.GREATER_THAN;
      case "before" -> ExpressionType.LESS_THAN;
      default -> ExpressionType.EQUALS;
    };
  }

  public Object returnRealValue(final FilterDTO filtro) throws ParseException {
    final String tipo = filtro.getTipo();
    final String valor = filtro.getValor();

    return switch (tipo) {
      case "string" -> valor;
      case "long" -> Long.parseLong(valor);
      case "integer" -> Integer.parseInt(valor);
      case "list" -> Arrays.asList(valor.split(","));
      case "boolean" -> Boolean.parseBoolean(valor);
      case "date" -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(valor, formatter);
        yield date.atStartOfDay();
      }
      case "dateTime" -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        yield LocalDateTime.parse(valor, formatter);
      }
      case "double" -> Double.parseDouble(valor);
      case "float" -> Float.parseFloat(valor);
      default -> null;
    };
  }

}
