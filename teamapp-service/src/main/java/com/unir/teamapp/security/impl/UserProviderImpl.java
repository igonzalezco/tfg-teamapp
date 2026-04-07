package com.unir.teamapp.security.impl;

import com.unir.teamapp.api.dto.UserDetailsDTO;
import com.unir.teamapp.api.security.utils.UserProvider;
import com.unir.teamapp.api.util.AppConstants;
import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.entity.UsuarioEquipo;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProviderImpl implements UserProvider {

  private final UsuarioRepository usuarioRepository;

  @Override
  public UserDetailsDTO getCurrentUser() {
    final SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext != null && securityContext.getAuthentication() != null
        && securityContext.getAuthentication().getPrincipal() instanceof UserDetailsDTO) {
      return (UserDetailsDTO) securityContext.getAuthentication().getPrincipal();
    }
    return null;
  }

  @Override
  public String getLogin() {
    final SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext != null && securityContext.getAuthentication() != null
        && securityContext.getAuthentication().getPrincipal() instanceof UserDetailsDTO) {
      return ((UserDetailsDTO) securityContext.getAuthentication().getPrincipal()).getUsername();
    }
    return null;
  }

  @Override
  public boolean isAdmin() {
    final SecurityContext securityContext = SecurityContextHolder.getContext();

    if (securityContext != null && securityContext.getAuthentication() != null
        && securityContext.getAuthentication().getPrincipal() instanceof UserDetailsDTO) {
      return ((UserDetailsDTO) securityContext.getAuthentication().getPrincipal()).getAuthorities().stream()
          .anyMatch(auth -> auth.getAuthority().equals(AppConstants.ADMINISTRADOR));
    }
    return false;
  }

  @Override
  public boolean hasPermission(String permission, Integer teamId) {
    final SecurityContext securityContext = SecurityContextHolder.getContext();
    if (securityContext != null && securityContext.getAuthentication() != null
        && securityContext.getAuthentication().getPrincipal() instanceof UserDetailsDTO) {
      Usuario usuario = usuarioRepository
          .findByEmail(((UserDetailsDTO) securityContext.getAuthentication().getPrincipal()).getUsername())
          .orElse(null);
      if (usuario != null) {
        for (UsuarioEquipo usuarioEquipo : usuario.getUsuarioEquipos()) {
          if (usuarioEquipo.getEquipo().getId().equals(teamId)
              && usuarioEquipo.getPermiso().getCodigo().equals(permission)) {
            return true;
          }
        }
      }
    }
    return false;
  }

}
