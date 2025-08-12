package com.unir.teamapp.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.UsuarioVistaDTO;
import com.unir.teamapp.api.service.UsuarioService;
import com.unir.teamapp.mapping.UsuarioMapper;
import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    private final UsuarioMapper usuarioMapper;

    @Override   
    public Optional<UsuarioVistaDTO> findUserByLogin(String login) {
        Optional<UsuarioVistaDTO> usuarioDTO = Optional.empty();
        final Optional<Usuario> optionalUser = usuarioRepository.findByEmail(login);
        if (optionalUser.isPresent()){
            usuarioDTO = Optional.of(usuarioMapper.asUsuarioDTO(optionalUser.get()));
        }
        
        return usuarioDTO;
    }

}
