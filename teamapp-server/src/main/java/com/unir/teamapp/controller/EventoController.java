package com.unir.teamapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unir.teamapp.api.dto.EventoDTO;
import com.unir.teamapp.api.dto.FiltersDTO;
import com.unir.teamapp.api.service.EventoService;
import com.unir.teamapp.api.util.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstants.URL_BASE_PATH)
public class EventoController {

  private final EventoService eventoService;

  @PostMapping(value = "/api/equipos/{equipoId}/eventos", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Crea un evento para un equipo", description = "Se solicitan los datos necesarios para la creación de un evento. Obligatorio fecha de título y fechas de inicio y fin", responses = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
      @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
  })
  public EventoDTO crearEvento(@PathVariable final Integer equipoId,
      @Valid @RequestBody final EventoDTO eventoDto) {
    return eventoService.crearEvento(equipoId, eventoDto);
  }

  @PostMapping(value = "/api/equipos/{equipoId}/eventos/search", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Obtiene los eventos de un equipo", description = "Se obtienen todos los eventos que coinciden con los filtros que se envían", responses = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
      @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
  })
  public Page<EventoDTO> buscarEventos(@PathVariable final Integer equipoId, @RequestBody final FiltersDTO filtros) {
    return eventoService.obtenerEventos(equipoId, filtros);
  }

  @DeleteMapping(value = "/api/equipos/{equipoId}/eventos/{eventoId}")
  @Operation(summary = "Elimina un evento", description = "Se elimina un evento de un equipo, se identificadores se obtienen por parametro.", responses = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "400", description = "Bad Request - Esto significa que el lado del cliente fallo en las validaciones de campos"),
      @ApiResponse(responseCode = "500", description = "Internal server error - Esto es un error generico del servidor")
  })
  public void deleteEvento(@PathVariable final Integer equipoId, @PathVariable final Integer eventoId) {
    eventoService.eliminarEvento(equipoId, eventoId);
  }

}
