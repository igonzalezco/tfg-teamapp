package com.unir.teamapp.persist.repository.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ComplexJpaRepository<T, ID extends Serializable> extends ComplexJpaSpecificationExecutor<T, ID> {

    Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraphType entityGraphType, String entityGraphName);

    List<T> findAll(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName);

    List<T> findAll(Specification<T> spec, Sort sort, EntityGraphType entityGraphType, String entityGraphName);

    T findOne(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName);

    Page<T> findAll(Specification<T> spec, Pageable pageable);
}
