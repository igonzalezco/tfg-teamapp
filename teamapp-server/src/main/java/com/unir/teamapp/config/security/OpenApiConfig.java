package com.unir.teamapp.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.build.version}")
    private String appVersion;

    
    /** 
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(
                new Info().version(appVersion).title(appName)
                        .description(appDescription));
    }
}
