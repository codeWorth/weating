package com.andrew.weating.entries;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        DbEntriesRepository.class,
        DbCommentsRepository.class
})
public class EntriesContext {

    @Bean
    public EntriesManager entriesManager(
            DbEntriesRepository entriesRepository,
            DbCommentsRepository commentsRepository,
            Clock clock
    ) {
        return new EntriesManager(entriesRepository, commentsRepository, clock);
    }
}
