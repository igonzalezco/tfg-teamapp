package com.unir.teamapp.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unir.teamapp.mapping.UserDetailsMapper;
import com.unir.teamapp.persist.entity.Usuario;
import com.unir.teamapp.persist.repository.jpa.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /** 
     * @param username
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Usuario retrievedUser = usuarioRepository.findByLoginWithRol(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se ha encontrado el usuario con nombre: " + username));
        return UserDetailsMapper.build(retrievedUser);
    }

}
