package com.kirinit.service.flower.delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://13.125.244.166")
                .allowedMethods("POST", "GET")
                .allowedHeaders("*")
                .allowedHeaders("Set-Cookie")
                .allowCredentials(false);
    }
}
