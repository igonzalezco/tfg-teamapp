package com.unir.teamapp.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.unir.teamapp.api.util.AppConstants;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    
    /** 
     * @return JWTAuthorizationFilter
     */
    @Bean
    public JWTAuthorizationFilter jwtAuthorizationFilter() {
        return new JWTAuthorizationFilter();
    }

    
    /** 
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    
    /** 
     * @param http
     * @param authManager
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
            .requestMatchers(
                AppConstants.URL_SWAGGER_API_DOCS,
                AppConstants.URL_SWAGGER_UI,
                AppConstants.URL_SWAGGER_UI_CUSTOM
            ).permitAll()
            .requestMatchers(HttpMethod.GET, AppConstants.URL_PUBLIC).permitAll()
            .requestMatchers(HttpMethod.POST, AppConstants.URL_AUTH_LOGIN).permitAll()
            .requestMatchers(HttpMethod.POST, AppConstants.URL_AUTH_REGISTER).permitAll()
            .anyRequest().authenticated()
        ).addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationManager(authManager);
        return http.build();
    }

    
    /** 
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

   
   /** 
    * @param userDetailsService
    * @return DaoAuthenticationProvider
    */
   @Bean
    public DaoAuthenticationProvider getDaoAuthProvider(final UserDetailsService userDetailsService) {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());

        return provider;
    }

    
    /** 
     * @param http
     * @param customProvider
     * @return AuthenticationManager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authManager(final HttpSecurity http, final CustomAuthenticationProvider customProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customProvider);

        return authenticationManagerBuilder.build();
    }
}
