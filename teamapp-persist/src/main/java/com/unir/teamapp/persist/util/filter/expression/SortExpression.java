package com.unir.teamapp.persist.util.filter.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort.Direction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;

public class SortExpression<T> {

  private final String campo;
  private final String orden;
  private Map<String, Join<?, ?>> mapJoins;

  public SortExpression(String campo, String orden) {
    this.campo = campo;
    this.orden = orden;
  }

  public void getSort(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder,
      Map<String, Join<?, ?>> joins) {

    if (root == null) {
      return;
    }

    mapJoins = joins;

    final Path<?> path = resolvePathForSort(root);

    if (path != null) {
      final List<Order> orders = new ArrayList<>(query.getOrderList());
      if (Direction.ASC.equals(Direction.fromString(orden))) {
        orders.add(criteriaBuilder.asc(path));
      } else if (Direction.DESC.equals(Direction.fromString(orden))) {
        orders.add(criteriaBuilder.desc(path));
      }
      query.orderBy(orders);
    }
  }

  private Path<?> resolvePathForSort(Root<T> root) {
    final String[] campoSplit = campo.split("\\.");

    if (campoSplit.length == 1) {
      return root.get(campoSplit[0]);
    }

    final String entityJoinName = String.join(".", Arrays.copyOfRange(campoSplit, 0, campoSplit.length - 1));
    final String sortKey = campoSplit[campoSplit.length - 1];

    if (StringUtils.isBlank(entityJoinName) || StringUtils.isBlank(sortKey)) {
      return null;
    }

    final Join<?, ?> join = getJoin(root, entityJoinName);
    return join != null ? join.get(sortKey) : null;
  }

  private Join<?, ?> getJoin(Root<T> root, String entityJoinName) {
    Join<?, ?> join = mapJoins.get(entityJoinName);
    if (join == null) {
      join = createJoin(entityJoinName.split("\\."), root);
    }
    return join;
  }

  private Join<?, ?> createJoin(String[] camposJoin, Root<T> root) {
    final JoinType joinType = JoinType.LEFT;
    Join<?, ?> join = null;

    int startIndex = 0;

    for (int i = camposJoin.length; i > 0; i--) {
      final String partialKey = String.join(".", Arrays.copyOfRange(camposJoin, 0, i));
      final Join<?, ?> existingJoin = mapJoins.get(partialKey);
      if (existingJoin != null) {
        join = existingJoin;
        startIndex = i;
        break;
      }
    }

    for (int i = startIndex; i < camposJoin.length; i++) {
      final String currentKey = String.join(".", Arrays.copyOfRange(camposJoin, 0, i + 1));
      join = (join == null) ? root.join(camposJoin[i], joinType) : join.join(camposJoin[i], joinType);
      mapJoins.put(currentKey, join);
    }

    return join;
  }
}
