package com.andrew.weating.entries;

import com.andrew.weating.comments.CommentsContext;
import com.andrew.weating.comments.CommentsManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        DbEntriesRepository.class,
        CommentsContext.class
})
public class EntriesContext {

    @Bean
    public EntriesManager entriesManager(
            DbEntriesRepository entriesRepository,
            CommentsManager commentsManager,
            Clock clock
    ) {
        return new EntriesManager(entriesRepository, commentsManager, clock);
    }
}
