package com.unir.teamapp.mapping;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.persist.entity.Evento;

@Mapper(componentModel = "spring")
public interface EventoMapper {

  @Mapping(target = "convocatorias", ignore = true)
  @Mapping(target = "necesidades", ignore = true)
  @Mapping(target = "equipo", ignore = true)
  Evento toEntity(EventoDTO src);

  EventoDTO asEventoDTO(Evento evento);

  List<EventoDTO> asEventoDTOList(List<Evento> eventos);
}
