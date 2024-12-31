package com.pet.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all paths
        .allowedOrigins("http://localhost:4200/","https://petapp-beryl.vercel.app/") 
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed methods
                .allowedHeaders("Content-Type", "Authorization") // Allow Authorization header
                .allowCredentials(true); // Allow cookies or credentials if necessary
    }
}
