package com.andrew.weating.entries;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public interface EntriesRepository {
    void add(Entry entry);
    void update(Entry entry, Function<Entry, Entry> func);
    void delete(UUID room, String submitter, String placeId);

    Optional<Entry> get(UUID room, String submitter, String placeId);
    Collection<Entry> getAll(UUID room);

    boolean containsRoom(UUID room);
}
