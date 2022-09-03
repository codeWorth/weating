package com.andrew.weating;

import com.andrew.weating.comments.CommentsContext;
import com.andrew.weating.entries.EntriesContext;
import com.andrew.weating.resources.CommentResource;
import com.andrew.weating.resources.EntriesResource;
import com.andrew.weating.resources.EntryResource;
import com.andrew.weating.resources.RoomResource;
import com.andrew.weating.util.ObjectMapperProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;

@Import({
        EntriesContext.class,
        CommentsContext.class,

        EntriesResource.class,
        EntryResource.class,
        CommentResource.class,
        RoomResource.class
})
public class ApplicationContext {
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
