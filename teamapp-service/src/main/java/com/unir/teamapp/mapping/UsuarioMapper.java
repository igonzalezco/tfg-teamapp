package com.unir.teamapp.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.unir.teamapp.api.dto.UsuarioVistaDTO;
import com.unir.teamapp.persist.entity.Usuario;

@Mapper(componentModel = "spring", uses = { RolMapper.class, UsuarioEquipoMapper.class })
public interface UsuarioMapper {

    @Named("asUserDTO")
    UsuarioVistaDTO asUsuarioVistaDTO(Usuario src);

}
