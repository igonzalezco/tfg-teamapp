package com.unir.teamapp.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.unir.teamapp.api.dto.EquipoVistaDTO;
import com.unir.teamapp.persist.entity.Equipo;

@Mapper(componentModel = "spring")
public interface EquipoMapper {

  @Mapping(target = "jugadores", ignore = true)
  @Mapping(target = "cuotas", ignore = true)
  @Mapping(target = "noticias", ignore = true)
  @Mapping(target = "encuestas", ignore = true)
  Equipo toEntity(EquipoVistaDTO src);

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "createdAt", source = "createdAt")
  @Mapping(target = "modifiedAt", expression = "java(java.time.LocalDateTime.now())")
  void updateFromDeleted(Equipo borrado, @MappingTarget Equipo nuevo);
}
