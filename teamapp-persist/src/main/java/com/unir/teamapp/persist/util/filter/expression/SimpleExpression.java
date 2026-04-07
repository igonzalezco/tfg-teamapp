package com.unir.teamapp.persist.util.filter.expression;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SimpleExpression extends GenericExpression {

  protected String entityJoinName = null;
  protected JoinType joinType = null;
  protected List<ParamExpression> paramExpressions = new ArrayList<>();

  private enum ComparisonOperator {
    GREATER_THAN, GREATER_OR_EQUALS, LESS_THAN, LESS_OR_EQUAL
  }

  public SimpleExpression(final String entityJoinName, final List<ParamExpression> paramExpressions) {
    this(entityJoinName, JoinType.INNER, paramExpressions);
  }

  public SimpleExpression(final String entityJoinName, ParamExpression paramExpression) {
    this(entityJoinName, JoinType.INNER, List.of(paramExpression));
  }

  public static SimpleExpression of(ParamExpression paramExpression) {
    if (paramExpression == null) {
      throw new IllegalArgumentException("ParamExpression no puede ser nulo");
    }
    return new SimpleExpression(null, null, List.of(paramExpression));
  }

  public static SimpleExpression of(List<ParamExpression> paramExpressions) {
    if (paramExpressions == null || paramExpressions.isEmpty()) {
      throw new IllegalArgumentException("La lista de ParamExpression no puede ser nula o vacía");
    }
    return new SimpleExpression(null, null, paramExpressions);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected Predicate generatePredicateDorCollectionFilter(final SimpleExpression simpleExpression, final Path path,
      final ParamExpression paramExpression, final CriteriaBuilder criteriaBuilder) {
    try {
      if (path != null && simpleExpression != null && paramExpression.getExpressionType() != null) {
        final ExpressionType expressionType = paramExpression.getExpressionType();
        final Object value = paramExpression.getValue();

        if (expressionType.name().equals(ExpressionType.IN.name()) && value != null) {
          return path.in((Collection) value);
        } else if (expressionType.name().equals(ExpressionType.NOT_IN.name()) && value != null) {
          return criteriaBuilder.not(path.in((Collection) value));
        } else if (expressionType.name().equals(ExpressionType.EQUALS.name()) && value == null) {
          return criteriaBuilder.isNull(path);
        } else if (expressionType.name().equals(ExpressionType.NOT_EQUALS.name()) && value == null) {
          return criteriaBuilder.isNotNull(path);
        } else if (expressionType.name().equals(ExpressionType.IS_NULL.name())) {
          return criteriaBuilder.isNull(path);
        } else if (expressionType.name().equals(ExpressionType.IS_NOT_NULL.name())) {
          return criteriaBuilder.isNotNull(path);
        } else if (expressionType.name().equals(ExpressionType.BETWEEN.name()) && value instanceof List) {
          return createBeetweenPredicate(path, value, criteriaBuilder);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Error al generar el Predicate para el filtro de colección", e);
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  protected Predicate generatePredicateForDateFilter(final Path path, final ParamExpression paramExpression,
      final CriteriaBuilder criteriaBuilder) {
    if (path != null && !LocalDateTime.class.isAssignableFrom(path.getJavaType())) {
      return null;
    }

    final ExpressionType expressionType = paramExpression.getExpressionType();
    final Object value = paramExpression.getValue();

    if (requiresNullHandling(expressionType, value)) {
      return handleNullCases(expressionType, criteriaBuilder, path);
    }

    return handleNonNullCases(expressionType, value, criteriaBuilder, path);
  }

  @SuppressWarnings("rawtypes")
  protected Predicate generatePredicateForBooleanFilter(final Path path, final ParamExpression paramExpression,
      final CriteriaBuilder criteriaBuilder) {
    if (path != null && !Boolean.class.isAssignableFrom(path.getJavaType())) {
      return null;
    }

    final ExpressionType expressionType = paramExpression.getExpressionType();
    final Object value = paramExpression.getValue();

    return switch (expressionType) {
      case EQUALS -> value != null ? criteriaBuilder.equal(path, value) : criteriaBuilder.isNull(path);
      case NOT_EQUALS -> value != null ? criteriaBuilder.notEqual(path, value) : criteriaBuilder.isNotNull(path);
      case IS_NULL -> criteriaBuilder.isNull(path);
      case IS_NOT_NULL -> criteriaBuilder.isNotNull(path);
      default -> null;
    };
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected Predicate generatePredicateForStringFilter(final Path path, final List<Path> pathComparatorList,
      final int idx, final ParamExpression paramExpression, final CriteriaBuilder criteriaBuilder) {
    if (path == null || !String.class.isAssignableFrom(path.getJavaType())) {
      return null;
    }

    final ExpressionType expressionType = paramExpression.getExpressionType();
    final Object value = paramExpression.getValue();
    final String fieldNameCompareColumn = paramExpression.getFieldNameCompareColumn();
    final Path pathComparator = CollectionUtils.isNotEmpty(pathComparatorList) && pathComparatorList.get(idx) != null
        ? pathComparatorList.get(idx)
        : null;

    if (expressionType == ExpressionType.EQUALS) {
      return generateEqualsPredicate(path, pathComparator, value, fieldNameCompareColumn, criteriaBuilder);
    }

    if (expressionType == ExpressionType.NOT_EQUALS) {
      return generateNotEqualsPredicate(path, pathComparator, value, fieldNameCompareColumn, criteriaBuilder);
    }

    return switch (expressionType) {
      case IN -> value != null ? path.in(value) : null;
      case NOT_IN -> value != null ? criteriaBuilder.not(path.in(value)) : null;
      case CONTAINS -> {
        if (value != null) {
          final String matchTerm = "%" + value.toString().toLowerCase() + "%";
          yield criteriaBuilder.like(criteriaBuilder.lower(path.as(String.class)), matchTerm);
        }
        yield null;
      }
      case IS_NULL -> criteriaBuilder.isNull(path);
      case IS_NOT_NULL -> criteriaBuilder.isNotNull(path);
      default -> null;
    };
  }

  @SuppressWarnings("rawtypes")
  protected Predicate generatePredicateForNumberFilter(final Path path, final List<Path> pathComparatorList,
      final int idx,
      final ParamExpression paramExpression, final CriteriaBuilder criteriaBuilder) {
    if (path == null || !Number.class.isAssignableFrom(path.getJavaType())) {
      return null;
    }

    final ExpressionType expressionType = paramExpression.getExpressionType();
    final Object value = paramExpression.getValue();
    final Path pathComparator = CollectionUtils.isNotEmpty(pathComparatorList) && pathComparatorList.get(idx) != null
        ? pathComparatorList.get(idx)
        : null;
    final String fieldNameCompareColumn = paramExpression.getFieldNameCompareColumn();

    return switch (expressionType) {
      case EQUALS -> value != null ? criteriaBuilder.equal(path, getNumberFromFilterAndPath(pathComparator, value))
          : criteriaBuilder.isNull(path);
      case NOT_EQUALS ->
        value != null ? criteriaBuilder.notEqual(path, getNumberFromFilterAndPath(pathComparator, value))
            : criteriaBuilder.isNotNull(path);
      case IN -> value != null ? path.in(value) : null;
      case NOT_IN -> value != null ? criteriaBuilder.not(path.in(value)) : null;
      case IS_NULL -> criteriaBuilder.isNull(path);
      case IS_NOT_NULL -> criteriaBuilder.isNotNull(path);
      case GREATER_THAN -> buildComparisonPredicate(criteriaBuilder, path, pathComparator, fieldNameCompareColumn,
          value, ComparisonOperator.GREATER_THAN);
      case GREATER_OR_EQUALS -> buildComparisonPredicate(criteriaBuilder, path, pathComparator, fieldNameCompareColumn,
          value, ComparisonOperator.GREATER_OR_EQUALS);
      case LESS_THAN -> buildComparisonPredicate(criteriaBuilder, path, pathComparator, fieldNameCompareColumn,
          value, ComparisonOperator.LESS_THAN);
      case LESS_OR_EQUALS -> buildComparisonPredicate(criteriaBuilder, path, pathComparator, fieldNameCompareColumn,
          value, ComparisonOperator.LESS_OR_EQUAL);
      default ->
        throw new IllegalStateException("Tipo de expresión no soportado para filtro numérico: " + expressionType);
    };

  }

  @Override
  @SuppressWarnings("rawtypes")
  public <R, J> Predicate getPredicate(Root<R> root, CriteriaBuilder criteriaBuilder, Map<String, Join<R, J>> joins) {
    if (root == null) {
      return null;
    }

    final List<Path> paths = resolvePahList(root, joins);
    final List<Predicate> wherePredicates = new ArrayList<>();
    final List<Predicate> joinPredicates = new ArrayList<>();

    for (int i = 0; i < paths.size(); i++) {
      final Path path = paths.get(i);
      final Object value = CollectionUtils.isNotEmpty(paramExpressions) && paramExpressions.get(i) != null
          ? paramExpressions.get(i).getValue()
          : null;

      if (path == null || value != null && value.toString().isEmpty()) {
        return null;
      }

      final Predicate predicate = buildPredicateByType(root, path, i, value, criteriaBuilder);

      if (getParamExpressions().get(i).getIsWherePredicate() == null
          || getParamExpressions().get(i).getIsWherePredicate().equals(Boolean.TRUE)) {
        wherePredicates.add(predicate);
      } else {
        joinPredicates.add(predicate);
      }
    }

    return getCombinedPredicate(wherePredicates, joinPredicates, criteriaBuilder, joins.get(entityJoinName));
  }

  @SuppressWarnings("rawtypes")
  private Number getNumberFromFilterAndPath(final Path path, final Object value) {
    Number matchTerm = null;
    Number numberValue = null;
    try {
      numberValue = (Number) value;
      if (path != null && Number.class.isAssignableFrom(path.getJavaType())) {
        if (Float.class.isAssignableFrom(path.getJavaType())) {
          matchTerm = numberValue.floatValue();
        } else if (Double.class.isAssignableFrom(path.getJavaType()) || Long.class.isAssignableFrom(path.getJavaType())
            || Integer.class.isAssignableFrom(path.getJavaType())) {
          matchTerm = numberValue.byteValue();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Error al convertir el valor a Number para el filtro numérico", e);
    }
    return matchTerm;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private Predicate buildComparisonPredicate(CriteriaBuilder criteriaBuilder, Path path, Path pathComparator,
      String fieldNameCompareColumn, Object value, ComparisonOperator operator) {

    if (fieldNameCompareColumn != null && pathComparator != null) {
      return switch (operator) {
        case GREATER_THAN -> criteriaBuilder.gt(path, pathComparator);
        case GREATER_OR_EQUALS -> criteriaBuilder.ge(path, pathComparator);
        case LESS_THAN -> criteriaBuilder.lt(path, pathComparator);
        case LESS_OR_EQUAL -> criteriaBuilder.le(path, pathComparator);
      };
    } else if (value != null) {
      final Number number = getNumberFromFilterAndPath(pathComparator, value);
      return switch (operator) {
        case GREATER_THAN -> criteriaBuilder.gt(path, number);
        case GREATER_OR_EQUALS -> criteriaBuilder.ge(path, number);
        case LESS_THAN -> criteriaBuilder.lt(path, number);
        case LESS_OR_EQUAL -> criteriaBuilder.le(path, number);
      };
    }
    return null;
  }

  private boolean requiresNullHandling(ExpressionType expressionType, Object value) {
    return expressionType == ExpressionType.EQUALS && value == null
        || expressionType == ExpressionType.NOT_EQUALS && value == null
        || expressionType == ExpressionType.IS_NULL
        || expressionType == ExpressionType.IS_NOT_NULL;
  }

  @SuppressWarnings("rawtypes")
  private Predicate handleNullCases(ExpressionType expressionType, CriteriaBuilder criteriaBuilder, Path path) {
    return switch (expressionType) {
      case EQUALS, IS_NULL -> criteriaBuilder.isNull(path);
      case NOT_EQUALS, IS_NOT_NULL -> criteriaBuilder.isNotNull(path);
      default -> null;
    };
  }

  @SuppressWarnings("rawtypes")
  private Predicate handleNonNullCases(ExpressionType expressionType, Object value, CriteriaBuilder criteriaBuilder,
      Path path) {
    @SuppressWarnings("unchecked")
    final Path<LocalDateTime> datePath = (Path<LocalDateTime>) path;

    return switch (expressionType) {
      case EQUALS -> {
        final LocalDateTime from = (LocalDateTime) value;
        LocalDateTime to = from.plusMinutes(1);
        yield criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(datePath, from),
            criteriaBuilder.lessThanOrEqualTo(datePath, to));
      }
      case GREATER_THAN -> criteriaBuilder.greaterThan(datePath, (LocalDateTime) value);
      case GREATER_OR_EQUALS -> criteriaBuilder.greaterThanOrEqualTo(datePath, (LocalDateTime) value);
      case LESS_THAN -> criteriaBuilder.lessThan(datePath, (LocalDateTime) value);
      case LESS_OR_EQUALS -> criteriaBuilder.lessThanOrEqualTo(datePath, (LocalDateTime) value);
      default -> null;
    };
  }

  private Predicate generateEqualsPredicate(Path<String> path, Path<String> pathComparator, Object value,
      String fieldNameCompareColumn, CriteriaBuilder criteriaBuilder) {
    if (value != null) {
      return criteriaBuilder.equal(criteriaBuilder.lower(path), ((String) value).toLowerCase());
    } else if (fieldNameCompareColumn != null) {
      return criteriaBuilder.equal(criteriaBuilder.lower(path), criteriaBuilder.lower(pathComparator));
    } else {
      return criteriaBuilder.isNotNull(path);
    }
  }

  private Predicate generateNotEqualsPredicate(Path<String> path, Path<String> pathComparator, Object value,
      String fieldNameCompareColumn, CriteriaBuilder criteriaBuilder) {
    if (value != null) {
      return criteriaBuilder.notEqual(criteriaBuilder.lower(path), ((String) value).toLowerCase());
    } else if (fieldNameCompareColumn != null) {
      return criteriaBuilder.notEqual(criteriaBuilder.lower(path), criteriaBuilder.lower(pathComparator));
    } else {
      return criteriaBuilder.isNotNull(path);
    }
  }

  @SuppressWarnings("rawtypes")
  private <R, J> List<Path> resolvePahList(final Root<R> root, final Map<String, Join<R, J>> joins) {
    List<String> fieldNameList = paramExpressions.stream().map(ParamExpression::getFieldName).toList();

    if (entityJoinName == null || entityJoinName.isEmpty()) {
      final Join<R, J> join = createJoinsWithFieldNameList(root, fieldNameList, joinType, joins);
      if (join != null) {
        fieldNameList = fieldNameList.stream().map(fieldName -> {
          final String[] keyFilters = fieldName.split("\\.");
          return keyFilters[keyFilters.length - 1];
        }).toList();
        return getJoinParamFilter(join, fieldNameList);
      } else {
        return BaseExpression.getParamFilter(root, fieldNameList);
      }
    } else {
      final Join<R, J> join = getJoin(root, entityJoinName, joinType, joins);
      return getJoinParamFilter(join, fieldNameList);
    }
  }

  private Predicate buildPredicateByType(final Root root, final Path path, final int idx, final Object value,
      final CriteriaBuilder criteriaBuilder) {
    final ParamExpression paramExpression = paramExpressions.get(idx);

    if (value != null && Collection.class.isAssignableFrom(value.getClass())
        || Collection.class.isAssignableFrom(path.getJavaType())) {
      return generatePredicateDorCollectionFilter(this, path, paramExpression, criteriaBuilder);
    } else if (Boolean.class.isAssignableFrom(value.getClass())) {
      return generatePredicateForBooleanFilter(path, paramExpression, criteriaBuilder);
    } else if (String.class.isAssignableFrom(path.getJavaType())) {
      final List<String> fieldNameCompareColumnList = paramExpressions.stream().map(ParamExpression::getFieldName)
          .filter(Objects::nonNull).toList();
      return generatePredicateForStringFilter(path, BaseExpression.getParamFilter(root, fieldNameCompareColumnList),
          idx, paramExpression, criteriaBuilder);
    } else if (Number.class.isAssignableFrom(path.getJavaType())) {
      final List<String> fieldNameCompareColumnList = paramExpressions.stream().map(ParamExpression::getFieldName)
          .filter(Objects::nonNull).toList();
      return generatePredicateForNumberFilter(path, BaseExpression.getParamFilter(root, fieldNameCompareColumnList),
          idx, paramExpression, criteriaBuilder);
    } else if (LocalDateTime.class.isAssignableFrom(path.getJavaType())) {
      return generatePredicateForDateFilter(path, paramExpression, criteriaBuilder);
    }
    return null;
  }

  private Predicate getCombinedPredicate(final List<Predicate> wherePredicates, final List<Predicate> joinPredicates,
      final CriteriaBuilder criteriaBuilder, final Join<?, ?> join) {
    Predicate wherePredicate = null;
    Predicate joinPredicate = null;

    for (final Predicate predicate : wherePredicates) {
      if (wherePredicate == null) {
        wherePredicate = predicate;
      } else {
        wherePredicate = criteriaBuilder.and(wherePredicate, predicate);
      }
    }

    for (final Predicate predicate : joinPredicates) {
      if (joinPredicate == null) {
        joinPredicate = predicate;
      } else {
        joinPredicate = criteriaBuilder.and(joinPredicate, predicate);
      }
    }

    if (joinPredicate != null) {
      join.on(joinPredicate);
    }

    return wherePredicate;
  }

  private <T extends Comparable<? super T>> Predicate createBeetweenPredicate(Path<T> path, Object value,
      CriteriaBuilder criteriaBuilder) {
    if (value instanceof Collection && ((Collection<?>) value).size() == 2) {
      final Iterator<?> iterator = ((Collection<?>) value).iterator();
      final Object startValue = iterator.next();
      final Object endValue = iterator.next();
      final Class<?> javaType = path.getJavaType();

      if (javaType.isInstance(startValue) && javaType.isInstance(endValue)) {
        final T start = (T) startValue;
        final T end = (T) endValue;
        return criteriaBuilder.between(path, start, end);
      }
    }
    return null;
  }

}
