package com.unir.teamapp.persist.util.filter.expression;

import java.util.Map;
import java.util.function.Predicate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

public interface Expression {

  String getId();

  <R, J> jakarta.persistence.criteria.Predicate getPredicate(final Root<R> root, final CriteriaBuilder criteriaBuilder,
      final Map<String, Join<R, J>> joins);

}
