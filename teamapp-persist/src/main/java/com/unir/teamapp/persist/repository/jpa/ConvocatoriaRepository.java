package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Convocatoria;

@Repository("convocatoriaRepository")
public interface ConvocatoriaRepository extends ComplexJpaRepository<Convocatoria, Integer> {

}
