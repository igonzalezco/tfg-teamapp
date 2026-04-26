package com.unir.teamapp.mapping;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.persist.entity.Equipo;
import com.unir.teamapp.persist.entity.Evento;

@Mapper(componentModel = "spring")
public interface EventoMapper {

  @Mapping(target = "convocatorias", ignore = true)
  @Mapping(target = "necesidades", ignore = true)
  @Mapping(target = "equipo", ignore = true)
  Evento toEntity(EventoDTO src);

  EventoDTO asEventoDTO(Evento evento);

  List<EventoDTO> asEventoDTOList(List<Evento> eventos);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "titulo", source = "titulo")
  @Mapping(target = "fechaInicio", source = "fechaInicio")
  @Mapping(target = "fechaFin", source = "fechaFin")
  @Mapping(target = "allDay", source = "allDay")
  @Mapping(target = "ubicacion", source = "ubicacion")
  @Mapping(target = "modifiedAt", expression = "java(java.time.LocalDateTime.now())")
  void updateFromDTO(EventoDTO eventoDTO, @MappingTarget Evento evento);
}
