package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Posicion;

@Repository("posicionRepository")
public interface PosicionRepository extends ComplexJpaRepository<Posicion, Integer> {

}
