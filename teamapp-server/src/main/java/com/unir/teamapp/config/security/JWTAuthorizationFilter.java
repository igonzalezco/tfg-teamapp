package com.unir.teamapp.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import com.unir.teamapp.api.util.AppConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter{

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${app.envars.jwt.token}")
    private String jwtToken;

    
    /** 
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String header = request.getHeader(AppConstants.TOKEN_HEADER);
            if (header == null || !header.startsWith(AppConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = request.getHeader(AppConstants.TOKEN_HEADER);
            UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        }
    }

     
     /** 
      * @return SecretKey
      */
     private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtToken.getBytes());
    }

    
    /** 
     * @param token
     * @return UsernamePasswordAuthenticationToken
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String token) {
        Jwt<JwsHeader, Claims> jwt = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token.replace(AppConstants.TOKEN_PREFIX, ""));

        Claims claims = jwt.getPayload();

        String username = claims.getSubject();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final Collection<SimpleGrantedAuthority> authorities = Optional.ofNullable(claims.get(AppConstants.TOKEN_AUTHORITIES, null)).map(_ -> {
            return Arrays.stream(claims.get(AppConstants.TOKEN_AUTHORITIES).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        }).orElseGet(ArrayList::new);
        
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

}
