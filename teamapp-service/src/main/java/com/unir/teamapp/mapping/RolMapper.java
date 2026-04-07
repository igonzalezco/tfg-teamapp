package com.unir.teamapp.mapping;

import org.mapstruct.Mapper;

import com.unir.teamapp.api.dto.RolDTO;
import com.unir.teamapp.persist.entity.Rol;

@Mapper(componentModel = "spring")
public interface RolMapper {

  RolDTO asRolDTO(Rol src);
}
