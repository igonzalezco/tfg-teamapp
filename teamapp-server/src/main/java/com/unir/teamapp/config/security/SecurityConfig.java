package com.unir.teamapp.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.unir.teamapp.api.util.AppConstants;
import com.unir.teamapp.service.util.JwtUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    
    /** 
     * @return JWTAuthorizationFilter
     */
    @Bean
    JWTAuthorizationFilter jwtAuthorizationFilter(final UserDetailsService userDetailsService, final JwtUtil jwtUtil) {
        return new JWTAuthorizationFilter(userDetailsService, jwtUtil);
    }

    
    /** 
     * @return PasswordEncoder
     */
    @Bean
    @Primary
    PasswordEncoder passwordEncoder() {
        return new CustomSHAPasswordEncoder();
    }
   
    /** 
     * @param http
     * @param authManager
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http, AuthenticationManager authManager, final JWTAuthorizationFilter jwtAuthorizationFilter) throws Exception {
        http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
            .requestMatchers(
                AppConstants.URL_SWAGGER_API_DOCS,
                AppConstants.URL_SWAGGER_UI,
                AppConstants.URL_SWAGGER_UI_CUSTOM
            ).permitAll()
            .requestMatchers(HttpMethod.GET, AppConstants.URL_PUBLIC).permitAll()
            .requestMatchers(HttpMethod.POST, AppConstants.URL_AUTH_LOGIN).permitAll()
            .requestMatchers(HttpMethod.POST, AppConstants.URL_AUTH_REGISTER).permitAll()
            .anyRequest().authenticated()
        ).addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationManager(authManager)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers.frameOptions(Customizer.withDefaults()).disable());

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    
    /** 
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

   
   /** 
    * @param userDetailsService
    * @return DaoAuthenticationProvider
    */
   @Bean
    DaoAuthenticationProvider getDaoAuthProvider(final UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /** 
     * @return PermissionEvaluator
     */
    @Bean
    PermissionEvaluator permissionEvaluator() {
        return new CustomPermissionEvaluator();
    }

    
    /** 
     * @return MethodSecurityExpressionHandler
     */
    @Bean
    MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        final DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator());
        return expressionHandler;
    }
}
