package com.spring.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class CorsConfig {

    @Bean
    public WebMvcConfigurer getCorsConfiguration() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configure CORS settings
                registry.addMapping("/**")
                        .allowedOrigins("*") // Allow all origins
                        .maxAge(4800) // Cache preflight response for 4800 seconds
                        .allowCredentials(false) // Do not allow credentials
                        .allowedMethods("POST", "GET", "OPTIONS", "DELETE", "PUT") // Allow specified HTTP methods
                        .allowedHeaders("origin", "Content-Type", "X-Requested-With", "X-File-Name", "x-mime-type",
                                "Accept-Encoding", "Authorization", "Content-Range", "Content-Disposition",
                                "Content-Description", "Access-Control-Request-Method", "Access-Control-Request-Headers"); // Allow specified headers
            }
        };
    }
}
