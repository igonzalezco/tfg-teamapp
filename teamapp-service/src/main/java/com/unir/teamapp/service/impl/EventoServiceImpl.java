package com.unir.teamapp.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.api.dto.FiltersDTO;
import com.unir.teamapp.api.security.SecurityUtils;
import com.unir.teamapp.api.service.EventoService;
import com.unir.teamapp.mapping.EquipoMapper;
import com.unir.teamapp.mapping.UsuarioEquipoMapper;
import com.unir.teamapp.persist.repository.jpa.EquipoRepository;
import com.unir.teamapp.persist.repository.jpa.PermisoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioEquipoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoServiceImpl implements EventoService {

  private final EquipoRepository equipoRepository;

  private final UsuarioEquipoRepository usuarioEquipoRepository;

  private final UsuarioRepository usuarioRepository;

  private final PermisoRepository permisoRepository;

  private final SecurityUtils securityUtils;

  private final EquipoMapper equipoMapper;

  private final UsuarioEquipoMapper usuarioEquipoMapper;

  @Override
  public EventoDTO crearEvento(EventoDTO evento) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'crearEvento'");
  }

  @Override
  public Page<EventoDTO> obtenerEventos(FiltersDTO filtros) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'obtenerEventos'");
  }

  @Override
  public void eliminarEvento(Integer equiopIdId, Integer eventoId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'eliminarEvento'");
  }

}
