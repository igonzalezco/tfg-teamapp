package com.unir.teamapp.api.service;

import org.springframework.data.domain.Page;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.api.dto.FiltersDTO;

import jakarta.validation.Valid;

public interface EventoService {

  EventoDTO crearEvento(Integer equipoId, EventoDTO evento);

  Page<EventoDTO> obtenerEventos(Integer equipoId, FiltersDTO filtros);

  EventoDTO obtenerEvento(Integer equipoId, Integer eventoId);

  void eliminarEvento(Integer equiopIdId, Integer eventoId);

  EventoDTO actualizarEvento(Integer equipoId, Integer eventoId, EventoDTO eventoDTO);
}
