package com.unir.teamapp.api.service;

import org.springframework.data.domain.Page;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.api.dto.FiltersDTO;

public interface EventoService {

  EventoDTO crearEvento(EventoDTO evento);

  Page<EventoDTO> obtenerEventos(FiltersDTO filtros);

  void eliminarEvento(Integer equiopIdId, Integer eventoId);
}
