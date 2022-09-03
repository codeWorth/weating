package com.andrew.weating.entries;

import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class EntriesManager {
    private final EntriesRepository entriesRepository;
    private final Clock clock;

    public void addEntry(UUID room, String submitter, String placeId, double rating, String review) {
        entriesRepository.add(new Entry(
                UUID.randomUUID(),
                room,
                submitter,
                rating,
                review,
                clock.instant(),
                placeId,
                clock.instant()
        ));
    }

    public void updateEntry(UUID room, String submitter, String placeId, Function<Entry, Entry> updateFunc) {
        entriesRepository.get(room, submitter, placeId)
                .map(updateFunc)
                .ifPresent(entriesRepository::add);
    }

    public void deleteEntry(UUID room, String submitter, String placeId) {
        entriesRepository.delete(room, submitter, placeId);
    }

    public Collection<Entry> getEntriesForRoom(UUID room) {
        return entriesRepository.getAll(room);
    }
}
