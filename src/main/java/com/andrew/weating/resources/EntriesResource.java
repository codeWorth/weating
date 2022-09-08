package com.andrew.weating.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.andrew.weating.entries.EntriesManager;
import com.andrew.weating.entries.Entry;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class EntriesResource {

    private final EntriesManager entriesManager;

    @GetMapping(value = "{roomId}/entries", produces = APPLICATION_JSON_VALUE)
    public Collection<Entry> entries(@PathVariable("roomId") String roomId) {
        return entriesManager.getEntriesForRoom(UUID.fromString(roomId));
    }
}
