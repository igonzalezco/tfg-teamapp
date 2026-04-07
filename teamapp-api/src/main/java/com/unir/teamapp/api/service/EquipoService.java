package com.unir.teamapp.api.service;

import com.unir.teamapp.api.dto.EquipoVistaDTO;
import com.unir.teamapp.api.dto.UsuarioEquipoDTO;

public interface EquipoService {

  UsuarioEquipoDTO crearEquipo(EquipoVistaDTO equipo);
}
