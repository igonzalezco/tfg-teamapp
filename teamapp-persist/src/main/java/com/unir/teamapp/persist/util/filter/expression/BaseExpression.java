package com.unir.teamapp.persist.util.filter.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.CollectionUtils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public abstract class BaseExpression implements Expression {

  protected String id;

  protected BaseExpression(String id) {
    if (id != null && !id.isEmpty()) {
      this.id = id;
    } else {
      this.id = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
  }

  protected BaseExpression() {
    this(null);
  }

  @Override
  public String getId() {
    return id;
  }

  protected Predicate getPredicateBaseForOperator(CriteriaBuilder criteriaBuilder, Predicate.BooleanOperator operator) {
    if (operator == null) {
      return null;
    }

    return switch (operator) {
      case AND -> criteriaBuilder.and();
      case OR -> criteriaBuilder.or();
      default -> null;
    };
  }

  @SuppressWarnings("rawtypes")
  protected static List<Path> getParamFilter(Root root, List<String> filterKeyList) {
    List<Path> paths = new ArrayList<>();

    if (CollectionUtils.isEmpty(filterKeyList)) {
      return Collections.emptyList();
    }

    for (final String filterKey : filterKeyList) {
      try {
        String[] filterKeyParts = filterKey.split("\\.");
        Path path = root;

        for (String part : filterKeyParts) {
          path = path.get(part);
        }

        paths.add(path);
      } catch (Exception e) {
        // Si no se encuentra el path, se ignora y se continúa con el siguiente
      }
    }
    return paths;
  }

  @SuppressWarnings("rawtypes")
  protected List<Path> getJoinParamFilter(Join join, List<String> filterKeyList) {
    List<Path> paths = new ArrayList<>();

    if (CollectionUtils.isEmpty(filterKeyList)) {
      return Collections.emptyList();
    }

    for (final String filterKey : filterKeyList) {
      try {
        String[] filterKeyParts = filterKey.split("\\.");

        if (filterKeyParts.length == 1) {
          paths.add(join.get(filterKeyParts[0]));
          continue;
        }

        Path path = join;
        for (String part : filterKeyParts) {
          path = path.get(part);
        }

        paths.add(path);
      } catch (Exception e) {
        // Si no se encuentra el path, se ignora y se continúa con el siguiente
      }
    }
    return paths;
  }

  @SuppressWarnings("rawtypes")
  public static List<ExpressionType> getDefaultExpressionType(Root root, List<String> filterKeyList) {
    final List<ExpressionType> expressionTypes = new ArrayList<>();
    if (root == null) {
      for (final String key : filterKeyList) {
        expressionTypes.add(ExpressionType.CONTAINS);
      }
      return expressionTypes;
    }
    final List<Path> paths = BaseExpression.getParamFilter(root, filterKeyList);
    for (final Path path : paths) {
      if (path == null) {
        expressionTypes.add(ExpressionType.CONTAINS);
        continue;
      }
      final Class<?> type = path.getJavaType();
      if (String.class.isAssignableFrom(type)
          || !Boolean.class.isAssignableFrom(type) && !Number.class.isAssignableFrom(type)) {
        expressionTypes.add(ExpressionType.CONTAINS);
      } else {
        expressionTypes.add(ExpressionType.EQUALS);
      }
    }
    return expressionTypes;
  }

  public void setId(String id) {
    this.id = id;
  }

  protected <R, J> Join<R, J> createJoinsWithFieldNameList(Root<R> root, List<String> fieldNameList, JoinType joinType,
      Map<String, Join<R, J>> joins) {
    if (CollectionUtils.isEmpty(fieldNameList)) {
      for (final String fieldName : fieldNameList) {
        final String[] keyFilters = fieldName.split("\\.");
        if (keyFilters.length > 1) {
          final String entityJoinName = String.join(".", Arrays.copyOfRange(keyFilters, 0, keyFilters.length - 1));
          return getJoin(root, entityJoinName, joinType, joins);
        }
      }
    }
    return null;
  }

  protected <R, J> Join<R, J> getJoin(Root<R> root, String entityJoinName, JoinType joinType,
      Map<String, Join<R, J>> joins) {
    Join<R, J> join = joins.get(entityJoinName);
    if (join != null) {
      return joins.get(entityJoinName);
    }
    final String[] keyFilters = entityJoinName.split("\\.");
    int startIndex = 0;

    for (int i = keyFilters.length; i > 0; i--) {
      final String joinName = String.join(".", Arrays.copyOfRange(keyFilters, 0, i));
      if (joins.containsKey(joinName)) {
        startIndex = i;
        break;
      }
    }

    for (int i = 0; i < keyFilters.length; i++) {
      final String joinName = String.join(".", Arrays.copyOfRange(keyFilters, 0, i + 1));
      final String joinKey = keyFilters[i];

      if (join == null) {
        join = root.join(joinKey, getSafeJoinType(joinType));
      } else {
        join = join.join(joinKey, getSafeJoinType(joinType));
      }
      joins.put(joinName, join);
    }

    return join;
  }

  private JoinType getSafeJoinType(JoinType joinType) {
    return joinType != null ? joinType : JoinType.LEFT;
  }
}
