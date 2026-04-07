package com.unir.teamapp.persist.util.filter;

import java.util.List;

import com.unir.teamapp.persist.util.filter.expression.ExpressionType;
import com.unir.teamapp.persist.util.filter.expression.GenericExpression;
import com.unir.teamapp.persist.util.filter.expression.ParamExpression;
import com.unir.teamapp.persist.util.filter.expression.SimpleExpression;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate.BooleanOperator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Filter {

  protected GenericExpression expression;
  protected BooleanOperator connOperatorToNextFilter = BooleanOperator.AND;

  public Filter(GenericExpression expression) {
    this(expression, BooleanOperator.AND);
  }

  public Filter(String entityJoin, String fieldName, ExpressionType expressionType, Object value) {
    final ParamExpression paramExpression = new ParamExpression(fieldName, value, expressionType, null, Boolean.TRUE);
    expression = new SimpleExpression(entityJoin, JoinType.INNER, List.of(paramExpression));
  }

  public Filter(String entityJoin, String fieldName, ExpressionType expressionType, Object value,
      BooleanOperator connOperatorToNextFilter) {
    this(entityJoin, fieldName, expressionType, value);
    this.connOperatorToNextFilter = connOperatorToNextFilter;
  }

  public Filter(String fieldName, ExpressionType expressionType, Object value) {
    final ParamExpression paramExpression = new ParamExpression(fieldName, value, expressionType, null, Boolean.TRUE);
    expression = new SimpleExpression(null, null, List.of(paramExpression));
  }

  public Filter(String fieldName, ExpressionType expressionType, Object value,
      BooleanOperator connOperatorToNextFilter) {
    this(fieldName, expressionType, value);
    this.connOperatorToNextFilter = connOperatorToNextFilter;
  }
}
