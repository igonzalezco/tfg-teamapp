package com.unir.teamapp.persist.util.filter;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.unir.teamapp.persist.util.filter.expression.ExpressionType;
import com.unir.teamapp.persist.util.filter.expression.SimpleExpression;
import com.unir.teamapp.persist.util.filter.expression.SortExpression;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate.BooleanOperator;
import jakarta.persistence.criteria.Root;

public interface SpecificationFilter<T> extends Specification<T> {

  void addFilter(Filter filter);

  void addFilter(String columnName, ExpressionType expressionType, BooleanOperator operator, Object value);

  void addFilter(String columnName, ExpressionType expressionType, Object value);

  void addFilter(String columnName, ExpressionType expressionType);

  Page<T> getData();

  void setData(Page<T> data);

  void setDistinct(Boolean distinct);

  Boolean getDistinct();

  void addSortExpression(SortExpression sortExpression);

  void addColumnExpression(String columnName, BooleanOperator booleanOperator, SimpleExpression simpleExpression);

  void buildPredicateForFilters(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);

  void buildPredicateForColumnExpressions(Root<T> root, CriteriaQuery<?> criteriaQuery,
      CriteriaBuilder criteriaBuilder);

}
