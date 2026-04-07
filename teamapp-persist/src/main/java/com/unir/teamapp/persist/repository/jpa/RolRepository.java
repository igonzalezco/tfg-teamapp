package com.unir.teamapp.persist.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Rol;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

@Repository("rolRepository")
public interface RolRepository extends ComplexJpaRepository<Rol, Integer> {

    Optional<Rol> findByCodigo(String codigo);

}
