package com.andrew.weating.entries;

import lombok.Value;
import lombok.With;

import java.time.Instant;
import java.util.UUID;

@Value
public class Entry {
    UUID id;
    UUID room;
    String submitter;
    @With
    double rating;
    @With
    String review;
    Instant createdAt;

    @With
    String placeId;
}
