package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.Convocatoria;

@Repository("convocatoriaRepository")
public interface ConvocatoriaRepository extends CustomJpaRepository<Convocatoria, Integer> {

}
