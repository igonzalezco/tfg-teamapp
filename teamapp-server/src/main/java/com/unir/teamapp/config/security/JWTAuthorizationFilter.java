package com.unir.teamapp.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.unir.teamapp.api.util.AppConstants;
import com.unir.teamapp.service.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter{

    private final UserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            final String header = request.getHeader(AppConstants.TOKEN_HEADER);
            if (header == null || !header.startsWith(AppConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = request.getHeader(AppConstants.TOKEN_HEADER);
            final String username = jwtUtil.getUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.isTokenValid(token, userDetails)) {
                    final UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            }
            
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }
    
    /** 
     * @param token
     * @return UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        final Claims claims = jwtUtil.getAllClaimsFromToken(token);
        final String username = claims.getSubject();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final Collection<SimpleGrantedAuthority> authorities = Optional.ofNullable((claims.get(AppConstants.TOKEN_AUTHORITIES)))
                    .map(_ -> Arrays.stream(claims.get(AppConstants.TOKEN_AUTHORITIES).toString().split(","))
                        .map(SimpleGrantedAuthority::new).toList())
                    .orElseGet(ArrayList::new);
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

}
