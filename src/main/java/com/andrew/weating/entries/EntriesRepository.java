package com.andrew.weating.entries;

import java.util.Collection;

public interface EntriesRepository {
    void add(Entry entry);

    Entry get(String room, String submitter, String placeId);
    Collection<Entry> getAll(String room);

    boolean containsRoom(String room);
}
