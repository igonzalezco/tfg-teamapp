package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.Posicion;

@Repository("posicionRepository")
public interface PosicionRepository extends CustomJpaRepository<Posicion, Integer> {

}
