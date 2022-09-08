package com.andrew.weating.resources;

import static com.andrew.weating.util.Collections.toMap;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.andrew.weating.places.Place;
import com.andrew.weating.places.PlacesManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PlacesResource {

    private final PlacesManager placesManager;

    @GetMapping(value = "{roomId}/places", produces = APPLICATION_JSON_VALUE)
    public Map<String, Place.Geometry> placesForRoom(@PathVariable("roomId") String roomId) {
        return placesManager.getPlaces(UUID.fromString(roomId))
                .entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().getGeometry()))
                .collect(toMap());
    }
}
