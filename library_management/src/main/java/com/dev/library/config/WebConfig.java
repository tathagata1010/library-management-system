package com.dev.library.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${web.config.allowed-origin-urls}")
    private String[] allowedOriginURLs;

    @Value("${web.config.max-age}")
    private int maxAge;




    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/books/**")
                .allowedOrigins(allowedOriginURLs)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(maxAge);
    }
}
