package com.unir.teamapp.persist.util.filter.expression;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParamExpression {

  String fieldName;
  Object value;
  ExpressionType expressionType;
  String fieldNameCompareColumn;
  Boolean isWherePredicate;

  @Override
  public int hashCode() {
    return Objects.hash(expressionType, fieldName, fieldNameCompareColumn, isWherePredicate, value);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ParamExpression other = (ParamExpression) obj;
    return Objects.equals(expressionType, other.expressionType)
        && Objects.equals(fieldName, other.fieldName)
        && Objects.equals(fieldNameCompareColumn, other.fieldNameCompareColumn)
        && Objects.equals(isWherePredicate, other.isWherePredicate)
        && Objects.equals(value, other.value);
  }

  @Override
  public String toString() {
    return "ParamExpression [fieldName=" + fieldName + ", value=" + value + ", expressionType=" + expressionType
        + ", fieldNameCompareColumn=" + fieldNameCompareColumn + ", isWherePredicate=" + isWherePredicate + "]";
  }
}
