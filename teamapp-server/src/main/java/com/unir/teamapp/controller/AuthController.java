package com.unir.teamapp.controller;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.teamapp.api.dto.LoginRequestDTO;
import com.unir.teamapp.api.dto.RegisterRequestDTO;
import com.unir.teamapp.api.dto.TokenUsuarioRolesDTO;
import com.unir.teamapp.api.exceptions.CustomException;
import com.unir.teamapp.api.exceptions.CustomLockedException;
import com.unir.teamapp.api.service.AuthService;
import com.unir.teamapp.api.util.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.URL_BASE_PATH)
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Loguea al usuario en el sistema", description= "Se solicita login y password para poder acceder al sistema.", responses = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
        @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
    })
    public TokenUsuarioRolesDTO login(@RequestBody @Valid @NotNull LoginRequestDTO loginRequest, final HttpServletRequest request) {
        TokenUsuarioRolesDTO salida = new TokenUsuarioRolesDTO();
        try {
            salida = authService.performLoginUsuario(loginRequest, request);
        } catch (final BadCredentialsException e) {
            throw new CustomException(e);
        } catch (final CustomLockedException e) {
            throw new CustomException(e.getLocalizedMessage(), e.getErrorResponse());
        } catch (final Exception e) {
            throw new CustomException(e);
        }
        
        return salida;
    }
    
    @PostMapping(value = "/registro", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registra al usuario en el sistema", description= "Se solicita varios datos para poder registrar al usuario en el sistema.", responses = {
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
        @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
    })
    public Boolean registro(@RequestBody @Valid @NotNull RegisterRequestDTO registerRequest) {
        Boolean salida = false;
        try {
           salida = authService.register(registerRequest);
        } catch (final BadCredentialsException e) {
            throw new CustomException(e);
        } catch (final CustomLockedException e) {
            throw new CustomException(e.getLocalizedMessage(), e.getErrorResponse());
        } catch (final Exception e) {
            throw new CustomException(e);
        }
        
        return salida;
    }


}
