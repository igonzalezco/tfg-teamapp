package com.unir.teamapp.persist.repository.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.unir.teamapp.persist.entity.Evento;
import com.unir.teamapp.persist.repository.common.CustomJpaRepository;

@Repository("eventoRepository")
public interface EventoRepository extends CustomJpaRepository<Evento, Integer> {

  Optional<Evento> findByIdAndEquipoIdAndDeletedAtIsNull(Integer idEvento, Integer idEquipo);

}
