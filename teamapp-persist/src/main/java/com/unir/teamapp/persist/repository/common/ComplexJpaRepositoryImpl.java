package com.unir.teamapp.persist.repository.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@NoRepositoryBean
public class ComplexJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements ComplexJpaRepository<T, ID> {

    protected final JpaEntityInformation<T, ID> entityInformation;
    protected final EntityManager em;
    protected final PersistenceProvider provider;

    public ComplexJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityInformation = entityInformation;
        this.em = entityManager;
        this.provider = PersistenceProvider.fromEntityManager(entityManager);
    }

    /** 
     * @return Class<T>
     */
    @Override
    public Class<T> getType() {
        return entityInformation.getJavaType();
    }

    /** 
     * @return EntityManager
     */
    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    /** 
     * @param spec
     * @param pageable
     * @param entityGraphType
     * @param entityGraphName
     * @return Page<T>
     */
    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable, EntityGraphType entityGraphType,
            String entityGraphName) {
        TypedQuery<T> query = super.getQuery(spec,pageable);
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return super.readPage(query, getDomainClass(), pageable, spec);
    }

    /** 
     * @param spec
     * @param entityGraphType
     * @param entityGraphName
     * @return List<T>
     */
    @Override
    public List<T> findAll(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = super.getQuery(spec, Sort.unsorted());
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return query.getResultList();
    }

    /** 
     * @param spec
     * @param sort
     * @param entityGraphType
     * @param entityGraphName
     * @return List<T>
     */
    @Override
    public List<T> findAll(Specification<T> spec, Sort sort, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = super.getQuery(spec, sort);
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return query.getResultList();
    }

    /** 
     * @param spec
     * @param entityGraphType
     * @param entityGraphName
     * @return T
     */
    @Override
    public T findOne(Specification<T> spec, EntityGraphType entityGraphType, String entityGraphName) {
        TypedQuery<T> query = super.getQuery(spec, Sort.unsorted());
        query.setHint(entityGraphType.getKey(), em.getEntityGraph(entityGraphName));
        return query.getSingleResult();
    }

}
