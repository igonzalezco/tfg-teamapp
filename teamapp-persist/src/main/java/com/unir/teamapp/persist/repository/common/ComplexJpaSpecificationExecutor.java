package com.unir.teamapp.persist.repository.common;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityManager;

@NoRepositoryBean
public interface ComplexJpaSpecificationExecutor<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    Class<T> getType();

    EntityManager getEntityManager();
}
