package com.unir.teamapp.config.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.unir.teamapp.api.dto.UserDetailsDTO;
import com.unir.teamapp.api.util.AppConstants;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
       if (authentication == null || !(authentication.getPrincipal() instanceof final UserDetailsDTO customUserDetails)) {
        return false;
       }

       return AppConstants.ADMINISTRADOR.equals(customUserDetails.getUsername());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        return false;
    }

}
