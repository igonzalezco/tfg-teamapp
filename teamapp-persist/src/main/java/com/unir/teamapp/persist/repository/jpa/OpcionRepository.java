package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Opcion;

@Repository("opcionRepository")
public interface OpcionRepository extends ComplexJpaRepository<Opcion, Integer> {

}
