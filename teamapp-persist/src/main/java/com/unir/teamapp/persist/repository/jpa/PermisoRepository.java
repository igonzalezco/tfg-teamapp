package com.unir.teamapp.persist.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Permiso;
import com.unir.teamapp.persist.repository.common.CustomJpaRepository;

@Repository("permisoRepository")
public interface PermisoRepository extends CustomJpaRepository<Permiso, Integer> {

  Optional<Permiso> findByCodigo(String codigo);

}
