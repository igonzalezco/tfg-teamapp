package com.unir.teamapp.mapping;

import org.mapstruct.Mapper;

import com.unir.teamapp.api.dto.UsuarioEquipoDTO;
import com.unir.teamapp.persist.entity.UsuarioEquipo;

@Mapper(componentModel = "spring")
public interface UsuarioEquipoMapper {

  UsuarioEquipoDTO asUsuarioEquipoDTO(UsuarioEquipo src);
}
