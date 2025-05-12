package com.unir.teamapp.api.service;

import com.unir.teamapp.api.dto.LoginRequestDTO;
import com.unir.teamapp.api.dto.RegisterRequestDTO;
import com.unir.teamapp.api.dto.TokenUsuarioRolesDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenUsuarioRolesDTO performLoginUsuario(final LoginRequestDTO loginRequest, HttpServletRequest request);

    Boolean register(final RegisterRequestDTO registerRequest);

}
