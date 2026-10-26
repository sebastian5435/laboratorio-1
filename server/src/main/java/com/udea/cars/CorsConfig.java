package com.udea.cars;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Permite que el frontend React (localhost:3000) consuma GET, POST y PUT
@Configuration
public class CorsConfig implements WebMvcConfigurer, RepositoryRestConfigurer {

    // http para npm run dev / serve, https cuando se usa el certificado SSL
    private static final String[] CLIENT_ORIGINS = {"http://localhost:3000", "https://localhost:3000"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(CLIENT_ORIGINS)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        cors.addMapping("/**")
                .allowedOrigins(CLIENT_ORIGINS)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
