package com.andrew.weating.entries;

import static generated.jooq.Tables.ENTRIES;
import static java.util.stream.Collectors.toList;

import com.andrew.weating.util.Hasher;
import generated.jooq.tables.records.EntriesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Collection;
import java.util.Optional;

@RequiredArgsConstructor
public class DbEntriesRepository implements EntriesRepository {
    private final DSLContext context;

    @Override
    public void add(Entry entry) {
        context.insertInto(ENTRIES)
                .set(toRecord(entry))
                .onDuplicateKeyUpdate()
                .set(toRecord(entry))
                .execute();
    }

    @Override
    public Optional<Entry> get(String room, String submitter, String placeId) {
        return context.selectFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room)
                        .and(ENTRIES.SUBMITTER.eq(submitter))
                        .and(ENTRIES.PLACE_ID.eq(placeId)))
                .fetchOptional()
                .map(this::fromRecord);
    }

    @Override
    public Collection<Entry> getAll(String room) {
        return context.selectFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room))
                .fetchStream()
                .map(this::fromRecord)
                .collect(toList());
    }

    @Override
    public boolean containsRoom(String room) {
        return context.selectFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room))
                .fetchOptional()
                .isPresent();
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

    private Entry fromRecord(EntriesRecord record) {
        return new Entry(
                record.getRoom(),
                record.getSubmitter(),
                record.getPlaceId(),
                record.getPlaceIdRefreshAt(),
                record.getRating(),
                record.getReview(),
                record.getCreatedAt()
        );
    }
}
