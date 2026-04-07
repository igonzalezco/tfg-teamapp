package com.unir.teamapp.persist.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Permiso;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

@Repository("permisoRepository")
public interface PermisoRepository extends ComplexJpaRepository<Permiso, Integer> {

  Optional<Permiso> findByCodigo(String codigo);

}
