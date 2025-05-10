package com.unir.teamapp.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private DaoAuthenticationProvider daoAuthenticationProvider;


    
    /** 
     * @param authentication
     * @return Authentication
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication auth = daoAuthenticationProvider.authenticate(authentication);
        return auth;
    }

    
    /** 
     * @param authentication
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
         return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
