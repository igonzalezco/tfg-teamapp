package com.unir.teamapp.service.util;

import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.unir.teamapp.api.util.AppConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;

@Component
public class JwtUtil {

    @Value("${app.envars.jwt.token}")
    private String secret;

    @Value("${app.envars.jwt.expires}")
    private Long expirationTime;

    public Claims getAllClaimsFromToken(final String token) {
        final MacAlgorithm alg = Jwts.SIG.HS512;
        final SecretKey key = alg.key().build();
        final SecretKeySpec secretKeySpec = new SecretKeySpec(Decoders.BASE64.decode(secret), key.getAlgorithm());
        final JwtParser jwtParser = Jwts.parser().verifyWith(secretKeySpec).build();
        return jwtParser.parseSignedClaims(token.replace(AppConstants.TOKEN_PREFIX, "")).getPayload();
    }

    public Date getExpirationDateFromToken(final String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public String generate(final Authentication authentication, final String username) {
        return doGenerateToken(authentication, username);
    }

    public String doGenerateToken(final Authentication authentication, final String username) {
        final String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        final JwtBuilder jwtBuilder = Jwts.builder().subject(username).signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
            .issuedAt(new Date(System.currentTimeMillis())).issuer(AppConstants.TOKEN_ISSUER).expiration(new Date(System.currentTimeMillis() + expirationTime));
        
        if (authorities != null && !authorities.isEmpty()) {
            jwtBuilder.claim("authorities", authorities);
        }

        return jwtBuilder.compact();
    }

    public String getUsername(final String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean isTokenValid(final String token, final UserDetails userDetails) {
        final String username = getUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(final String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}
