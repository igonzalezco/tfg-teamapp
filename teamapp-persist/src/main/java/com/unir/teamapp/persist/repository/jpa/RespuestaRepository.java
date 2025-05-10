package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Respuesta;

@Repository("respuestaRepository")
public interface RespuestaRepository extends ComplexJpaRepository<Respuesta, Integer> {

}
