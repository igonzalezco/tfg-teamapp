package com.unir.teamapp.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.teamapp.api.dto.EquipoVistaDTO;
import com.unir.teamapp.api.dto.UsuarioEquipoDTO;
import com.unir.teamapp.api.service.EquipoService;
import com.unir.teamapp.api.util.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.URL_BASE_PATH)
public class EquipoController {

  private final EquipoService equipoService;

  @PostMapping(value = "/api/equipos", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Crea un equipo para un usuario", description = "Se solicita el nombre y descripción del equipo para poder crearlo.", responses = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
      @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
  })
  public UsuarioEquipoDTO crearEquipo(
      @Valid @RequestBody final EquipoVistaDTO equipoVistaDto) {
    return equipoService.crearEquipo(equipoVistaDto);
  }

  @DeleteMapping(value = "/api/equipos/{equipoId}")
  @Operation(summary = "Elimina un equipo", description = "Se solicita el id del equipo para poder eliminarlo.", responses = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
      @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
  })
  public void deleteEquipo(@PathVariable final Integer equipoId) {
    equipoService.eliminarEquipo(equipoId);
  }

}
