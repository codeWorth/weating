package com.andrew.weating.comments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        DbCommentsRepository.class
})
public class CommentsContext {
    @Bean
    public CommentsManager commentManager(
            DbCommentsRepository commentsRepository,
            Clock clock
    ) {
        return new CommentsManager(commentsRepository, clock);
    }
}
