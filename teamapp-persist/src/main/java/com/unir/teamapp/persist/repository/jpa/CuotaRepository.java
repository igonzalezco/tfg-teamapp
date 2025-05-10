package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Cuota;
import com.unir.teamapp.persist.repository.common.ComplexJpaRepository;

@Repository("cuotaRepository")
public interface CuotaRepository extends ComplexJpaRepository<Cuota, Integer> {

}
