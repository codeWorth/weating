package com.andrew.weating.entries;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface EntriesRepository {
    void add(Entry entry);
    void delete(UUID room, String submitter, String placeId);

    Optional<Entry> get(UUID room, String submitter, String placeId);
    Collection<Entry> getAll(UUID room);

    boolean containsRoom(UUID room);
}
