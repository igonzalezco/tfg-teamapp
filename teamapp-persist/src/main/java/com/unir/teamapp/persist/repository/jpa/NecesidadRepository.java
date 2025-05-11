package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Necesidad;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

@Repository("necesidadRepository")
public interface NecesidadRepository extends ComplexJpaRepository<Necesidad, Integer> {

}
