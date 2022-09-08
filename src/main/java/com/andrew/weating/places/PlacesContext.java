package com.andrew.weating.places;

import com.andrew.weating.entries.EntriesContext;
import com.andrew.weating.entries.EntriesManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.time.Clock;

@Import({
        DbPlacesRepository.class,
        EntriesContext.class
})
public class PlacesContext {
    @Bean
    public PlacesManager placesManager(
            DbPlacesRepository placesRepository,
            EntriesManager entriesManager,
            Clock clock
    ) {
        return new PlacesManager(placesRepository, entriesManager, clock);
    }
}
