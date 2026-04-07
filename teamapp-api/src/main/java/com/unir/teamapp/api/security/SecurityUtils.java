package com.unir.teamapp.api.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.unir.teamapp.api.dto.UserDetailsDTO;
import com.unir.teamapp.api.security.utils.UserProvider;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

  @Lazy
  private final UserProvider userProvider;

  public UserDetailsDTO getCurrentUser() {
    return userProvider.getCurrentUser();
  }

  public String getLogin() {
    return userProvider.getLogin();
  }

  public boolean isAdmin() {
    return userProvider.isAdmin();
  }

  public boolean hasPermission(String permission, Integer teamId) {
    return userProvider.hasPermission(permission, teamId);
  }
}
