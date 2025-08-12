package com.unir.teamapp.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.unir.teamapp.api.dto.UsuarioVistaDTO;
import com.unir.teamapp.persist.entity.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Named("asUserDTO")
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "usuarioEquipos", ignore = true)
    UsuarioVistaDTO asUsuarioDTO(Usuario src);

}