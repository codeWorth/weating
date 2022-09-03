package com.andrew.weating.entries;

import lombok.Value;

import java.time.Instant;

@Value
public class Entry {
    String room;
    String submitter;

    String placeId;
    Instant placeIdRefresh;

    int rating;
    String review;
    Instant createdAt;
}
