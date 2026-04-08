package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Cuota;
import com.unir.teamapp.persist.repository.common.CustomJpaRepository;

@Repository("cuotaRepository")
public interface CuotaRepository extends CustomJpaRepository<Cuota, Integer> {

}
