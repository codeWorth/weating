package com.andrew.weating.places;

import static generated.jooq.Tables.PLACES;

import com.andrew.weating.util.Hasher;
import generated.jooq.tables.records.PlacesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.util.Optional;

@RequiredArgsConstructor
public class DbPlacesRepository implements PlacesRepository {

    private final DSLContext context;

    @Override
    public void add(Place place) {
        context.insertInto(PLACES)
                .set(toRecord(place))
                .onDuplicateKeyUpdate()
                .set(toRecord(place))
                .execute();
    }

    @Override
    public Optional<Place> get(String placeId) {
        return context.selectFrom(PLACES)
                .where(PLACES.ID.eq(placeId))
                .fetchOptional(this::fromRecord);
    }

    @Override
    public void remove(String placeId) {
        context.deleteFrom(PLACES)
                .where(PLACES.ID.eq(placeId))
                .execute();
    }

    private PlacesRecord toRecord(Place place) {
        return new PlacesRecord(
                place.getId(),
                Hasher.hash(place.getId()),
                place.getRefreshAt(),
                place.getGeometry()
        );
    }

    private Place fromRecord(PlacesRecord record) {
        return new Place(
                record.getId(),
                record.getRefreshAt(),
                record.getGeometry()
        );
    }
}
