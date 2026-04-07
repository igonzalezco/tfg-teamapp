package com.unir.teamapp.persist.util.filter.expression;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.unir.teamapp.api.exceptions.CustomException;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Predicate.BooleanOperator;

public class ColumnExpression extends BaseExpression {

  protected List<SimpleExpression> comparators = null;
  protected BooleanOperator connOperatorBetweenExpressions = BooleanOperator.AND;

  protected ColumnExpression() {
  }

  protected ColumnExpression(String columnName) {
    id = columnName;
  }

  public ColumnExpression(String columnName, BooleanOperator booleanOperator, List<SimpleExpression> comparators) {
    super(columnName);
    connOperatorBetweenExpressions = booleanOperator;
    this.comparators = comparators;
  }

  public ColumnExpression(String columnName, List<SimpleExpression> comparators) {
    this(columnName, BooleanOperator.AND, comparators);
  }

  public ColumnExpression(String columnName, SimpleExpression... comparators) {
    this(columnName, BooleanOperator.AND, Arrays.asList(comparators));
  }

  @Override
  public <R, J> Predicate getPredicate(Root<R> root, CriteriaBuilder criteriaBuilder, Map<String, Join<R, J>> joins) {
    Predicate predicate = null;
    if (comparators != null && !comparators.isEmpty()) {
      for (final SimpleExpression columnComparator : comparators) {
        predicate = columnComparator.getPredicate(root, criteriaBuilder, joins);
      }
    }
    return predicate;
  }

  public String getName() {
    return getId();
  }

  public String getColumnName() {
    return getId();
  }

  public void setColumnName(String columnName) {
    id = columnName;
  }

  public List<SimpleExpression> getComparators() {
    return comparators;
  }

  public void setComparators(List<SimpleExpression> comparators) {
    this.comparators = comparators;
  }

  public BooleanOperator getConnOperatorBetweenExpressions() {
    return connOperatorBetweenExpressions;
  }

  public void setConnOperatorBetweenExpressions(BooleanOperator connOperatorBetweenExpressions) {
    if (connOperatorBetweenExpressions == null) {
      throw new CustomException("");
    }
    this.connOperatorBetweenExpressions = connOperatorBetweenExpressions;
  }

  public void updateColumnExpression(ColumnExpression filterByColumn) {
    setColumnName(filterByColumn.getColumnName());
    setComparators(filterByColumn.getComparators());
  }

  public static Integer getIndexByColumnName(List<ColumnExpression> columnExpressions, String columnName) {
    if (columnExpressions != null) {
      for (int i = 0; i < columnExpressions.size(); i++) {
        if (columnExpressions.get(i).getColumnName().equals(columnName)) {
          return i;
        }
      }
    }
    return -1;
  }
}
