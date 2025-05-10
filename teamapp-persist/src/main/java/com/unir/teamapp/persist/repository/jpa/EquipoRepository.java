package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Equipo;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

@Repository("equipoRepository")
public interface EquipoRepository extends ComplexJpaRepository<Equipo, Integer> {

}
