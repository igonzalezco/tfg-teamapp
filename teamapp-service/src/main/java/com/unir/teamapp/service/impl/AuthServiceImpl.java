package com.unir.teamapp.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.LoginRequestDTO;
import com.unir.teamapp.api.dto.RegisterRequestDTO;
import com.unir.teamapp.api.dto.TokenUsuarioRolesDTO;
import com.unir.teamapp.api.dto.UsuarioVistaDTO;
import com.unir.teamapp.api.service.AuthService;
import com.unir.teamapp.api.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioService usuarioService;

    private final AuthenticationManager authenticationManager;

    @Override
    public TokenUsuarioRolesDTO performLoginUsuario(LoginRequestDTO loginRequest, HttpServletRequest request) {
        final Optional<UsuarioVistaDTO> optionalUser = usuarioService.findUserByLogin(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
        
        final Authentication authentication = authenticate(loginRequest);
        
        TokenUsuarioRolesDTO tokenUsuarioRolesDTO = TokenUsuarioRolesDTO.builder().build();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //TODO generar UsernamePasswordAuthenticationToken
        //TODO Generar Token
        //TODO Obtener los Equipos
        //TODO Completar el tokenUsuarioRolesDTO con el usuarioVista, los equipos y el token

        return tokenUsuarioRolesDTO;
    }

    @Override
    public Boolean register(RegisterRequestDTO registerRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'register'");
    }

    private Authentication authenticate(LoginRequestDTO loginRequest) {
        try {
            final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch (final LockedException ex) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

}
