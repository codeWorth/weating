package com.andrew.weating.entries;

import static generated.jooq.Tables.ENTRIES;

import com.andrew.weating.util.Hasher;
import generated.jooq.tables.records.EntriesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.math.BigDecimal;
import java.util.Collection;

@RequiredArgsConstructor
public class DbEntriesRepository implements EntriesRepository {
    private final DSLContext context;

    @Override
    public void add(Entry entry) {
//        context.insertInto(ENTRIES)
    }

    @Override
    public Entry get(String room, String submitter, String placeId) {
        return null;
    }

    @Override
    public Collection<Entry> getAll(String room) {
        return null;
    }

    @Override
    public boolean containsRoom(String room) {
        return false;
    }

    private EntriesRecord toRecord(Entry entry) {
        return new EntriesRecord(
                entry.getRoom(),
                entry.getSubmitter(),
                entry.getCreatedAt(),
                entry.getPlaceId(),
                Hasher.hash(entry.getPlaceId()),
                entry.getPlaceIdRefresh(),
                entry.getRating(),
                entry.getReview()
        );
    }
}
