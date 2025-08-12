package com.unir.teamapp.api.service;

import java.util.Optional;

import com.unir.teamapp.api.dto.UsuarioVistaDTO;

public interface UsuarioService {

    Optional<UsuarioVistaDTO> findUserByLogin(final String login);

}
