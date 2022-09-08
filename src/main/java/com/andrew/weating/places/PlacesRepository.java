package com.andrew.weating.places;

import java.util.Optional;

public interface PlacesRepository {
    void add(Place place);
    Optional<Place> get(String placeId);
    void remove(String placeId);
}
