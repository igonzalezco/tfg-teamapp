package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.Respuesta;

@Repository("respuestaRepository")
public interface RespuestaRepository extends CustomJpaRepository<Respuesta, Integer> {

}
