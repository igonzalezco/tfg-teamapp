package com.unir.teamapp.service.impl;

import com.unir.teamapp.service.util.JwtUtil;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.LoginRequestDTO;
import com.unir.teamapp.api.dto.RegisterRequestDTO;
import com.unir.teamapp.api.dto.TokenUsuarioRolesDTO;
import com.unir.teamapp.api.dto.UsuarioVistaDTO;
import com.unir.teamapp.api.service.AuthService;
import com.unir.teamapp.api.service.UsuarioService;
import com.unir.teamapp.api.util.AppConstants;
import com.unir.teamapp.persist.entity.Rol;
import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.repository.jpa.RolRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;

    private final UsuarioService usuarioService;

    private final AuthenticationManager authenticationManager;

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenUsuarioRolesDTO performLoginUsuario(LoginRequestDTO loginRequest, HttpServletRequest request) {
        final Optional<UsuarioVistaDTO> optionalUser = usuarioService.findUserByLogin(loginRequest.getUsername());
        if (optionalUser.isEmpty()) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        final Authentication authentication = authenticate(loginRequest);

        TokenUsuarioRolesDTO tokenUsuarioRolesDTO = TokenUsuarioRolesDTO.builder().build();

        SecurityContextHolder.getContext().setAuthentication(authentication);

        tokenUsuarioRolesDTO.setToken(jwtUtil.doGenerateToken(authentication, loginRequest.getUsername()));

        tokenUsuarioRolesDTO.setUsuario(optionalUser.get());

        return tokenUsuarioRolesDTO;
    }

    @Override
    @Transactional
    public Boolean register(RegisterRequestDTO request) {
        String emailNormalizado = normalizarEmail(request.getEmail());

        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Rol rolUsuario = rolRepository.findByCodigo(AppConstants.USUARIO)
                .orElseThrow(() -> new IllegalStateException("No existe el rol por defecto USUARIO"));

        Usuario usuario = new Usuario();
        usuario.setEmail(emailNormalizado);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rolUsuario);
        usuario.setCreatedAt(LocalDateTime.now());

        usuario = usuarioRepository.save(usuario);

        return usuario.getId() != null;
    }

    private Authentication authenticate(LoginRequestDTO loginRequest) {
        try {
            final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch (final LockedException ex) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }
    }

    private String normalizarEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

}
