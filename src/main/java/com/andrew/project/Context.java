package com.andrew.project;

import com.andrew.project.resources.TestResource;
import com.andrew.project.util.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Import({
        TestResource.class
})
public class Context {
    @Bean
    public WebMvcConfigurer corsConfigurer(
            @Value("${server.allowed.origin}") String allowedOrigin
    ) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigin)
                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE");
            }
        };
    }
    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        return ObjectMapperProvider.get();
    }
}
