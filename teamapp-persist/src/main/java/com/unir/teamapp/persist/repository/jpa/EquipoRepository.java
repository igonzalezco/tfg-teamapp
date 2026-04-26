package com.unir.teamapp.persist.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Equipo;
import com.unir.teamapp.persist.repository.common.CustomJpaRepository;

@Repository("equipoRepository")
public interface EquipoRepository extends CustomJpaRepository<Equipo, Integer> {

  Optional<Equipo> getByNombreIgnoreCase(String nombre);

}
