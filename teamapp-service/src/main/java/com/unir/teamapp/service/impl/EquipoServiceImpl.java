package com.unir.teamapp.service.impl;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.api.dto.EquipoVistaDTO;
import com.unir.teamapp.api.dto.UserDetailsDTO;
import com.unir.teamapp.api.dto.UsuarioEquipoDTO;
import com.unir.teamapp.api.exceptions.CustomException;
import com.unir.teamapp.api.security.SecurityUtils;
import com.unir.teamapp.api.service.EquipoService;
import com.unir.teamapp.api.util.AppConstants;
import com.unir.teamapp.mapping.EquipoMapper;
import com.unir.teamapp.mapping.UsuarioEquipoMapper;
import com.unir.teamapp.persist.entity.Equipo;
import com.unir.teamapp.persist.entity.Permiso;
import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.entity.UsuarioEquipo;
import com.unir.teamapp.persist.repository.jpa.EquipoRepository;
import com.unir.teamapp.persist.repository.jpa.PermisoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioEquipoRepository;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipoServiceImpl implements EquipoService {

  private final EquipoRepository equipoRepository;

  private final UsuarioEquipoRepository usuarioEquipoRepository;

  private final UsuarioRepository usuarioRepository;

  private final PermisoRepository permisoRepository;

  private final SecurityUtils securityUtils;

  private final EquipoMapper equipoMapper;

  private final UsuarioEquipoMapper usuarioEquipoMapper;

  @Override
  public UsuarioEquipoDTO crearEquipo(final EquipoVistaDTO equipo) {
    final UserDetailsDTO sessionUser = securityUtils.getCurrentUser();
    final Usuario usuario = usuarioRepository.findByEmail(sessionUser.getUsername())
        .orElseThrow(() -> new CustomException("Usuario no encontrado", HttpStatus.NOT_FOUND));

    if (equipoRepository.existsByNombreIgnoreCase(equipo.getNombre())) {
      throw new CustomException("Ya existe un equipo con ese nombre", HttpStatus.BAD_REQUEST);
    }

    final Permiso permisoAdministrador = permisoRepository
        .findByCodigo(AppConstants.PERMISO_ADMINISTRADOR)
        .orElseThrow(() -> new CustomException(
            "No se ha encontrado el permiso de administrador de equipo"));

    Equipo equipoBD = equipoMapper.toEntity(equipo);
    equipoBD.setCreatedAt(LocalDateTime.now());

    equipoBD = equipoRepository.save(equipoBD);

    final UsuarioEquipo usuarioEquipo = UsuarioEquipo.builder().equipo(equipoBD).usuario(usuario)
        .permiso(permisoAdministrador).build();

    return usuarioEquipoMapper.asUsuarioEquipoDTO(usuarioEquipoRepository.save(usuarioEquipo));
  }
}
