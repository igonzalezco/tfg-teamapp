package com.unir.teamapp.mapping;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.unir.teamapp.api.dto.UserDetailsDTO;
import com.unir.teamapp.api.exceptions.CustomException;
import com.unir.teamapp.persist.entity.Usuario;

public class UserDetailsMapper {

    private UserDetailsMapper() {
        throw new CustomException("Clase de utilidades, no se permiten instancias");
    }

    
    /** 
     * @param usuario
     * @return UserDetails
     */
    public static UserDetails build(final Usuario usuario) {
        return new UserDetailsDTO(usuario.getEmail(), usuario.getPassword(), UserDetailsMapper.getAuthorities(usuario), usuario.getId());
    }

    
    /** 
     * @param usuario
     * @return Set<? extends GrantedAuthority>
     */
    private static Set<? extends GrantedAuthority> getAuthorities(final Usuario usuario) {
        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRol().getCodigo()));
        return authorities;
    }

}
