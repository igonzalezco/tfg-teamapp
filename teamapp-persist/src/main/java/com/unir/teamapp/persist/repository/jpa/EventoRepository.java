package com.unir.teamapp.persist.repository.jpa;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.repository.common.CustomJpaRepository;
import com.unir.teamapp.persist.entity.Evento;

@Repository("eventoRepository")
public interface EventoRepository extends CustomJpaRepository<Evento, Integer> {

}
