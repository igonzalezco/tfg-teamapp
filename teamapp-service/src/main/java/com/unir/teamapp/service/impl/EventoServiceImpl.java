package com.unir.teamapp.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.api.dto.FiltersDTO;
import com.unir.teamapp.api.exceptions.CustomException;
import com.unir.teamapp.api.security.SecurityUtils;
import com.unir.teamapp.api.service.EquipoService;
import com.unir.teamapp.api.service.EventoService;
import com.unir.teamapp.mapping.EventoMapper;
import com.unir.teamapp.persist.entity.Equipo;
import com.unir.teamapp.persist.entity.Evento;
import com.unir.teamapp.persist.repository.jpa.EquipoRepository;
import com.unir.teamapp.persist.repository.jpa.EventoRepository;
import com.unir.teamapp.persist.repository.jpa.PermisoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioEquipoRepository;
import com.unir.teamapp.persist.util.filter.FilterManagement;
import com.unir.teamapp.persist.util.filter.SpecificationFilter;
import com.unir.teamapp.persist.util.filter.expression.ExpressionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoServiceImpl implements EventoService {

  private final EventoRepository eventoRepository;

  private final UsuarioEquipoRepository usuarioEquipoRepository;

  private final EquipoRepository equipoRepository;

  private final EquipoService equipoService;

  private final PermisoRepository permisoRepository;

  private final SecurityUtils securityUtils;

  private final EventoMapper eventoMapper;

  @Override
  public EventoDTO crearEvento(Integer equipoId, EventoDTO eventoDTO) {

    equipoService.checkGestionEquipoForSessionUser(equipoId);

    validateCreateEvent(eventoDTO);

    final Equipo equipo = equipoRepository.findById(equipoId)
        .orElseThrow(() -> new CustomException("No existe el equipo indicado."));

    final Evento event = eventoMapper.toEntity(eventoDTO);
    event.setEquipo(equipo);
    event.setCreatedAt(LocalDateTime.now());

    final Evento savedEvent = eventoRepository.save(event);

    return eventoMapper.asEventoDTO(savedEvent);
  }

  @Override
  public Page<EventoDTO> obtenerEventos(Integer equipoId, FiltersDTO filtros) {
    final FilterManagement<Evento> filterManagement = new FilterManagement<>(filtros, "titulo", Sort.Direction.ASC);
    final SpecificationFilter<Evento> spec = filterManagement.getSpecificationFilter();
    spec.addFilter("equipo.id", ExpressionType.EQUALS, equipoId);

    final Page<Evento> pageEventos = eventoRepository.findAll(spec, filterManagement.getPageable(),
        EntityGraphType.FETCH, "obtenerEventos");

    return pageEventos.map(eventoMapper::asEventoDTO);
  }

  @Override
  public void eliminarEvento(Integer equiopIdId, Integer eventoId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'eliminarEvento'");
  }

  private void validateCreateEvent(final EventoDTO eventoDTO) {
    if (eventoDTO.getFechaFin().isBefore(eventoDTO.getFechaInicio())) {
      throw new CustomException("La fecha de fin no puede ser anterior a la fecha de inicio.");
    }
  }

}
