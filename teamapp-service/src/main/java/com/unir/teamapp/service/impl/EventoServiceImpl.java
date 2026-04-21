package com.unir.teamapp.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.api.dto.FiltersDTO;
import com.unir.teamapp.api.security.SecurityUtils;
import com.unir.teamapp.api.service.EventoService;
import com.unir.teamapp.mapping.EventoMapper;
import com.unir.teamapp.mapping.UsuarioEquipoMapper;
import com.unir.teamapp.persist.entity.Evento;
import com.unir.teamapp.persist.repository.jpa.EventoRepository;
import com.unir.teamapp.persist.repository.jpa.PermisoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioEquipoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;
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

  private final UsuarioRepository usuarioRepository;

  private final PermisoRepository permisoRepository;

  private final SecurityUtils securityUtils;

  private final EventoMapper eventoMapper;

  private final UsuarioEquipoMapper usuarioEquipoMapper;

  @Override
  public EventoDTO crearEvento(EventoDTO evento) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'crearEvento'");
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

}
