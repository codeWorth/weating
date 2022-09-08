package com.andrew.weating.places;

import static com.andrew.weating.util.Collections.toMap;

import com.andrew.weating.entries.EntriesManager;
import com.andrew.weating.entries.Entry;
import com.andrew.weating.places.PlacesManager.PlaceDetailsResponse.Status;
import com.andrew.weating.util.Collections;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.SignalType;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
public class PlacesManager {
    private static final String GOOGLE_API_KEY = "AIzaSyDwlhYzRP9fkdG2F4sp9p5_7mqebIJVjok";
    private static final Duration PLACE_INFO_EXPIRATION = Duration.ofDays(30);
    private final PlacesRepository placesRepository;
    private final EntriesManager entriesManager;
    private final Clock clock;

    public Map<String, Place> getPlaces(UUID room) {
        Collection<Entry> entries = entriesManager.getEntriesForRoom(room);

        Map<String, CompletableFuture<Place>> placeFutures = entries.stream()
                .map(Entry::getPlaceId)
                .distinct()
                .map(placeId -> Map.entry(placeId, getPlace(placeId)))
                .collect(toMap());

        CompletableFuture.allOf(placeFutures.values().toArray(CompletableFuture[]::new)).join();
        Map<String, Place> places = placeFutures.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), sneakyGetPlace(entry.getValue())))
                .collect(toMap());

        entries.forEach(entry -> {
            Place place = places.get(entry.getPlaceId());
            if (place.getId().equals(entry.getPlaceId())) {
                entriesManager.updateEntry(entry, e -> e.withPlaceId(place.getId()));
            }
        });

        return places;
    }

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    private Place sneakyGetPlace(CompletableFuture<Place> future) {
        return future.get();
    }

    private CompletableFuture<Place> getPlace(String placeId) {
        return placesRepository.get(placeId)
                .flatMap(place -> needsRefresh(place) ? Optional.empty() : Optional.of(place))
                .map(CompletableFuture::completedFuture)
                .orElseGet(() -> downloadPlace(placeId));
    }

    private boolean needsRefresh(Place place) {
        return place.getRefreshAt().plus(PLACE_INFO_EXPIRATION).isBefore(clock.instant());
    }

    @SneakyThrows
    private CompletableFuture<Place> downloadPlace(String placeId) {
        CompletableFuture<Place> future = new CompletableFuture<>();
        WebClientProvider.getProvider().request().get()
                .get()
                .uri(UriComponentsBuilder.fromUriString("place/details/json")
                        .queryParam("fields", "geometry,place_id")
                        .queryParam("place_id", placeId)
                        .queryParam("key", GOOGLE_API_KEY)
                        .build()
                        .toUriString())
                .retrieve()
                .bodyToMono(PlaceDetailsResponse.class)
                .doOnSuccess(response -> {
                    log.info("Downloaded place details: {}", response);
                    if (!response.getStatus().equals(Status.OK)) {
                        future.completeExceptionally(new IOException(
                                "downloadPlace failed with status " + response.getStatus() + " and error " + response.getError_message()));
                    } else {
                        PlaceDetailsResponse.Result result = response.getResult();
                        if (!placeId.equals(result.getPlace_id())) {
                            placesRepository.remove(placeId);
                        }
                        Place place = new Place(result.getPlace_id(), clock.instant(), result.getGeometry());
                        placesRepository.add(place);
                        future.complete(place);
                    }
                })
                .doOnError(error -> {
                    log.error("downloadPlace failed with error: ", error);
                })
                .doFinally(signal -> {
                    if (!signal.equals(SignalType.ON_COMPLETE)) {
                        future.completeExceptionally(new IOException("downloadPlace failed with signal " + signal));
                    }
                })
                .subscribe();
        return future;
    }

    @Data
    static class PlaceDetailsResponse {
        Status status;
        String error_message;
        Result result;

        enum Status {
            OK, ZERO_RESULTS, NOT_FOUND, INVALID_REQUEST, OVER_QUERY_LIMIT, REQUEST_DENIED, UNKNOWN_ERROR
        }

        @Value
        static class Result {
            String place_id;
            Place.Geometry geometry;
        }
    }
}
