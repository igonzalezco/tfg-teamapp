package com.unir.teamapp.persist.util.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;

import com.unir.teamapp.api.exceptions.CustomException;
import com.unir.teamapp.persist.util.filter.expression.ColumnExpression;
import com.unir.teamapp.persist.util.filter.expression.ExpressionType;
import com.unir.teamapp.persist.util.filter.expression.SimpleExpression;
import com.unir.teamapp.persist.util.filter.expression.SortExpression;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Predicate.BooleanOperator;
import jakarta.persistence.criteria.Root;

public class SpecificationFilterBean<T> implements SpecificationFilter<T> {

  protected Predicate predicate = null;
  protected List<ColumnExpression> columnExpressions = null;
  protected Page<T> data = null;
  protected List<Filter> filters = null;
  protected Boolean distinct = true;
  protected List<SortExpression> sortExpressions = null;
  protected Map<String, Join<T, ?>> joins = new HashMap<>();

  public SpecificationFilterBean() {
    columnExpressions = new ArrayList<>();
    sortExpressions = new ArrayList<>();
    filters = new ArrayList<>();
    joins = new HashMap<>();
  }

  @Override
  public void addFilter(Filter filter) {
    if (filters != null) {
      filters.add(filter);
    }
  }

  @Override
  public void addFilter(String fieldName, ExpressionType expressionType, BooleanOperator operator, Object value) {
    final Filter filter = new Filter(fieldName, expressionType, value, operator);
    addFilter(filter);
  }

  @Override
  public void addFilter(String columnName, ExpressionType expressionType, Object value) {
    final Filter filter = new Filter(columnName, expressionType, value);
    addFilter(filter);
  }

  @Override
  public void addFilter(String columnName, ExpressionType expressionType) {
    final Filter filter = new Filter(columnName, expressionType, null);
    addFilter(filter);
  }

  @Override
  public Page<T> getData() {
    return data;
  }

  @Override
  public void setData(Page<T> data) {
    this.data = data;
  }

  @Override
  public void setDistinct(Boolean distinct) {
    this.distinct = distinct;
  }

  @Override
  public Boolean getDistinct() {
    return distinct;
  }

  @Override
  public void addSortExpression(SortExpression sortExpression) {
    if (sortExpression != null) {
      sortExpressions.add(sortExpression);
    }
  }

  @Override
  public void addColumnExpression(String columnName, BooleanOperator booleanOperator,
      SimpleExpression simpleExpression) {
    booleanOperator = (booleanOperator == null) ? BooleanOperator.AND : booleanOperator;
    final ColumnExpression columnExpression = new ColumnExpression(columnName, simpleExpression);

    if (columnExpressions == null) {
      columnExpressions = new ArrayList<>();
    }
    final int i = ColumnExpression.getIndexByColumnName(columnExpressions, columnExpression.getColumnName());
    if (i >= 0) {
      columnExpressions.get(i).updateColumnExpression(columnExpression);
    } else {
      columnExpressions.add(columnExpression);
    }
  }

  @Override
  public Predicate toPredicate(Root<T> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    query.distinct(distinct);
    predicate = criteriaBuilder.conjunction();

    joins = new HashMap<>();
    buildPredicateForColumnExpressions(root, query, criteriaBuilder);
    buildPredicateForFilters(root, query, criteriaBuilder);
    buildSort(root, query, criteriaBuilder);

    return predicate;
  }

  @Override
  public void buildPredicateForFilters(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    try {
      if (CollectionUtils.isEmpty(filters)) {
        return;
      }

      final List<Predicate> orPredicates = new ArrayList<>();
      final List<Predicate> andPredicates = new ArrayList<>();

      for (final Filter filter : filters) {
        processFilter(root, criteriaBuilder, filter, orPredicates, andPredicates);
      }

      combinePredicates(criteriaBuilder, orPredicates, andPredicates);
    } catch (final Exception ex) {

    }
  }

  @Override
  public void buildPredicateForColumnExpressions(Root<T> root, CriteriaQuery<?> criteriaQuery,
      CriteriaBuilder criteriaBuilder) {
    if (CollectionUtils.isEmpty(columnExpressions)) {
      return;
    }

    final List<Predicate> orPredicates = new ArrayList<>();
    final List<Predicate> andPredicates = new ArrayList<>();

    for (final ColumnExpression columnExpression : columnExpressions) {
      final Predicate newPredicate;
      try {
        newPredicate = columnExpression.<T, Object>getPredicate(root, criteriaBuilder,
            (Map<String, Join<T, Object>>) (Map<?, ?>) joins);
      } catch (final Exception ex) {
        final String columnName = columnExpression.getColumnName();
        final String errorMessage = String.format("Error generando predicado par la columna '%s'", columnName);
        throw new CustomException(errorMessage + ": " + ex.getMessage(), ex);
      }

      if (newPredicate == null) {
        continue;
      }

      if (columnExpression.getConnOperatorBetweenExpressions() == BooleanOperator.OR) {
        orPredicates.add(newPredicate);
      } else {
        andPredicates.add(newPredicate);
      }
    }

    if (!orPredicates.isEmpty()) {
      final Predicate orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
      predicate = criteriaBuilder.and(predicate, orPredicate);
    }
    if (!andPredicates.isEmpty()) {
      final Predicate andPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
      predicate = criteriaBuilder.and(predicate, andPredicate);
    }
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void buildSort(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    try {
      if (CollectionUtils.isNotEmpty(sortExpressions)) {
        for (final SortExpression sortExpression : sortExpressions) {
          sortExpression.getSort(root, query, criteriaBuilder, joins);
        }
      }
    } catch (final Exception ex) {
      // throw
    }
  }

  @SuppressWarnings("unchecked")
  private void processFilter(Root<T> root, CriteriaBuilder criteriaBuilder, Filter filter, List<Predicate> orPredicates,
      List<Predicate> andPredicates) {
    if (filter.getExpression() == null) {
      return;
    }

    final Predicate newPredicate = filter.getExpression().<T, Object>getPredicate(root, criteriaBuilder,
        (Map<String, Join<T, Object>>) (Map<?, ?>) joins);

    if (newPredicate == null) {
      return;
    }

    if (filter.getConnOperatorToNextFilter() == BooleanOperator.OR) {
      orPredicates.add(newPredicate);
    } else {
      andPredicates.add(newPredicate);
    }
  }

  private void combinePredicates(CriteriaBuilder criteriaBuilder, List<Predicate> orPredicates,
      List<Predicate> andPredicates) {
    if (!orPredicates.isEmpty()) {
      final Predicate orPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
      predicate = criteriaBuilder.and(predicate, orPredicate);
    }
    if (!andPredicates.isEmpty()) {
      final Predicate andPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
      predicate = criteriaBuilder.and(predicate, andPredicate);
    }
  }

}
