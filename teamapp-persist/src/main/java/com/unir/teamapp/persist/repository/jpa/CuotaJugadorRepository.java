package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.CuotaJugador;

@Repository("cuotaJugadorRepository")
public interface CuotaJugadorRepository extends CustomJpaRepository<CuotaJugador, Integer> {

}
