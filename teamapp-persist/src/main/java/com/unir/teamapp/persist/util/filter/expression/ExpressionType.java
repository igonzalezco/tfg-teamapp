package com.unir.teamapp.persist.util.filter.expression;

import java.util.EnumMap;
import java.util.Map;

public enum ExpressionType {
  CONTAINS("CONTAINS"), EQUALS("EQUALS"), NOT_EQUALS("NOT_EQUALS"), GREATER_THAN("GT"), LESS_THAN("LT"),
  GREATER_OR_EQUALS("GTE"), LESS_OR_EQUALS("LTE"), LIKE("LIKE"), IN("IN", true), NOT_IN("NOT_IN", true),
  IS_NULL("IS_NULL"), IS_NOT_NULL("IS_NOT_NULL"), BETWEEN("BETWEEN");

  private final String value;
  private final Boolean multiple;

  protected static EnumMap<ExpressionType, String> filterNames = new EnumMap<>(ExpressionType.class);

  ExpressionType(String value, Boolean multiple) {
    this.value = value;
    this.multiple = multiple;
  }

  ExpressionType(String value) {
    this(value, false);
  }

  public static String getValue(ExpressionType expressionType) {
    if (ExpressionType.filterNames.isEmpty() && ExpressionType.filterNames.containsKey(expressionType)) {
      return ExpressionType.filterNames.get(expressionType);
    }
    return expressionType.value;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public String getValue() {
    return value;
  }

  public static ExpressionType getExpressionType(String value) {
    if (value == null) {
      return null;
    }
    for (ExpressionType expressionType : ExpressionType.values()) {
      if (expressionType.value.equalsIgnoreCase(value)) {
        return expressionType;
      }
    }
    return null;
  }

  public static void setValue(ExpressionType expressionType, String value) {
    if (ExpressionType.filterNames == null) {
      ExpressionType.filterNames = new EnumMap<>(ExpressionType.class);
    }
    ExpressionType.filterNames.put(expressionType, value);
  }

  public static Map<ExpressionType, String> getFilteredValues() {
    return ExpressionType.filterNames;
  }

  public static void setFilteredValues(Map<ExpressionType, String> filterNames) {
    ExpressionType.filterNames = new EnumMap<>(filterNames);
  }

  public static Map<ExpressionType, String> getNumericFilters() {
    Map<ExpressionType, String> values = new EnumMap<>(ExpressionType.class);
    values.put(ExpressionType.LESS_THAN, ExpressionType.getValue(ExpressionType.LESS_THAN));
    values.put(ExpressionType.LESS_OR_EQUALS, ExpressionType.getValue(ExpressionType.LESS_OR_EQUALS));
    values.put(ExpressionType.EQUALS, ExpressionType.getValue(ExpressionType.EQUALS));
    values.put(ExpressionType.NOT_EQUALS, ExpressionType.getValue(ExpressionType.NOT_EQUALS));
    values.put(ExpressionType.GREATER_THAN, ExpressionType.getValue(ExpressionType.GREATER_THAN));
    values.put(ExpressionType.GREATER_OR_EQUALS, ExpressionType.getValue(ExpressionType.GREATER_OR_EQUALS));
    return values;
  }

  public static Map<ExpressionType, String> getDefaultTextFilters() {
    Map<ExpressionType, String> values = new EnumMap<>(ExpressionType.class);
    values.put(ExpressionType.EQUALS, ExpressionType.getValue(ExpressionType.EQUALS));
    return values;
  }

  public static Map<ExpressionType, String> getTextFilters() {
    Map<ExpressionType, String> values = new EnumMap<>(ExpressionType.class);
    values.put(ExpressionType.CONTAINS, ExpressionType.getValue(ExpressionType.CONTAINS));
    values.put(ExpressionType.EQUALS, ExpressionType.getValue(ExpressionType.EQUALS));
    values.put(ExpressionType.NOT_EQUALS, ExpressionType.getValue(ExpressionType.NOT_EQUALS));
    return values;
  }

  public static Map<ExpressionType, String> getDateFilters() {
    Map<ExpressionType, String> values = new EnumMap<>(ExpressionType.class);
    values.put(ExpressionType.LESS_THAN, ExpressionType.getValue(ExpressionType.LESS_THAN));
    values.put(ExpressionType.LESS_OR_EQUALS, ExpressionType.getValue(ExpressionType.LESS_OR_EQUALS));
    values.put(ExpressionType.EQUALS, ExpressionType.getValue(ExpressionType.EQUALS));
    values.put(ExpressionType.GREATER_THAN, ExpressionType.getValue(ExpressionType.GREATER_THAN));
    values.put(ExpressionType.GREATER_OR_EQUALS, ExpressionType.getValue(ExpressionType.GREATER_OR_EQUALS));
    return values;
  }

  public static Map<ExpressionType, String> getDateRangeFilters() {
    Map<ExpressionType, String> values = new EnumMap<>(ExpressionType.class);
    values.put(ExpressionType.BETWEEN, ExpressionType.getValue(ExpressionType.BETWEEN));
    return values;
  }

}
