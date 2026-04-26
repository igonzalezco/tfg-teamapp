package com.unir.teamapp.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.UsuarioVistaDTO;
import com.unir.teamapp.api.service.UsuarioService;
import com.unir.teamapp.mapping.UsuarioMapper;
import com.unir.teamapp.persist.repository.jpa.UsuarioEquipoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final UsuarioEquipoRepository usuarioEquipoRepository;

    private final UsuarioMapper usuarioMapper;

    @Override
    public Optional<UsuarioVistaDTO> findUserByLogin(String login) {
        return usuarioRepository.findByEmailWithRol(login)
                .map(usuario -> {
                    usuario.setUsuarioEquipos(
                            usuarioEquipoRepository.findByUsuarioId(usuario.getId()));
                    return usuarioMapper.asUsuarioVistaDTO(usuario);
                });
    }

}
