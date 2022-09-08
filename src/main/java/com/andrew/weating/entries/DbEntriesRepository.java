package com.andrew.weating.entries;

import static generated.jooq.Tables.ENTRIES;
import static java.util.stream.Collectors.toList;

import com.andrew.weating.util.Hasher;
import generated.jooq.tables.records.EntriesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class DbEntriesRepository implements EntriesRepository {
    private final DSLContext context;

    @Override
    public void add(Entry entry) {
        context.insertInto(ENTRIES)
                .set(toRecord(entry))
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void update(Entry entry, Function<Entry, Entry> func) {
        context.update(ENTRIES)
                .set(toRecord(func.apply(entry)))
                .where(ENTRIES.ROOM.eq(entry.getRoom())
                        .and(ENTRIES.SUBMITTER.eq(entry.getSubmitter()))
                        .and(ENTRIES.PLACE_ID.eq(entry.getPlaceId())))
                .execute();
    }

    @Override
    public void delete(UUID room, String submitter, String placeId) {
        context.deleteFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room)
                        .and(ENTRIES.SUBMITTER.eq(submitter))
                        .and(ENTRIES.PLACE_ID.eq(placeId)))
                .execute();
    }

    @Override
    public Optional<Entry> get(UUID room, String submitter, String placeId) {
        return context.selectFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room)
                        .and(ENTRIES.SUBMITTER.eq(submitter))
                        .and(ENTRIES.PLACE_ID.eq(placeId)))
                .fetchOptional()
                .map(this::fromRecord);
    }

    @Override
    public Collection<Entry> getAll(UUID room) {
        return context.selectFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room))
                .fetchStream()
                .map(this::fromRecord)
                .collect(toList());
    }

    @Override
    public boolean containsRoom(UUID room) {
        return context.selectFrom(ENTRIES)
                .where(ENTRIES.ROOM.eq(room))
                .fetchOptional()
                .isPresent();
    }

    private EntriesRecord toRecord(Entry entry) {
        return new EntriesRecord(
                entry.getId(),
                entry.getRoom(),
                entry.getSubmitter(),
                entry.getCreatedAt(),
                entry.getPlaceId(),
                Hasher.hash(entry.getPlaceId()),
                entry.getRating(),
                entry.getReview()
        );
    }

    private Entry fromRecord(EntriesRecord record) {
        return new Entry(
                record.getId(),
                record.getRoom(),
                record.getSubmitter(),
                record.getRating(),
                record.getReview(),
                record.getCreatedAt(),
                record.getPlaceId()
        );
    }
}
