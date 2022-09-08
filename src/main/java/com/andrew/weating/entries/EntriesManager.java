package com.andrew.weating.entries;

import com.andrew.weating.comments.CommentsManager;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class EntriesManager {
    private final EntriesRepository entriesRepository;
    private final CommentsManager commentsManager;
    private final Clock clock;

    public void addEntry(UUID room, String submitter, String placeId, double rating, String review) {
        entriesRepository.add(new Entry(
                UUID.randomUUID(),
                room,
                submitter,
                rating,
                review,
                clock.instant(),
                placeId
        ));
    }

    public void updateEntry(Entry entry, Function<Entry, Entry> updateFunc) {
        entriesRepository.update(entry, updateFunc);
    }

    public void updateEntry(UUID room, String submitter, String placeId, Function<Entry, Entry> updateFunc) {
        entriesRepository.get(room, submitter, placeId)
                .ifPresent(entry -> updateEntry(entry, updateFunc));
    }

    public void deleteEntry(UUID room, String submitter, String placeId) {
        entriesRepository.delete(room, submitter, placeId);

        boolean hasEntryForPlace = getEntriesForRoom(room)
                .stream()
                .anyMatch(entry -> entry.getPlaceId().equals(placeId));
        if (!hasEntryForPlace) {
            commentsManager.deleteComments(room, placeId);
        }
    }

    public Collection<Entry> getEntriesForRoom(UUID room) {
        return entriesRepository.getAll(room);
    }
}
