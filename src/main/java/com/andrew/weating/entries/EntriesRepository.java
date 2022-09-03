package com.andrew.weating.entries;

import java.util.Collection;
import java.util.Optional;

public interface EntriesRepository {
    void add(Entry entry);

    Optional<Entry> get(String room, String submitter, String placeId);
    Collection<Entry> getAll(String room);

    boolean containsRoom(String room);
}
