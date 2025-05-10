package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;
import com.unir.teamapp.persist.entity.Rol;

@Repository("rolRepository")
public interface RolRepository extends ComplexJpaRepository<Rol, Integer> {

}
