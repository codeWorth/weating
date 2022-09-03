package com.andrew.weating.entries;

import lombok.Value;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Value
public class Entry {
    UUID id;

    String room;
    String submitter;

    String placeId;
    @With
    Instant placeIdRefresh;

    @With
    double rating;
    @With
    String review;
    Instant createdAt;
}
