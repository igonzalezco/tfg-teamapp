package com.unir.teamapp.api.security.utils;

import com.unir.teamapp.api.dto.UserDetailsDTO;

public interface UserProvider {

  UserDetailsDTO getCurrentUser();

  String getLogin();

  boolean isAdmin();

  boolean hasPermission(String permission, Integer teamId);
}
