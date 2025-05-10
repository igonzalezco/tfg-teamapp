package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Encuesta;

@Repository("encuestaRepository")
public interface EncuestaRepository extends ComplexJpaRepository<Encuesta, Integer> {

}
