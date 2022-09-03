package com.andrew.weating.entries;

import lombok.Value;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

@Value
public class Comment {
    UUID id;
    UUID room;
    UUID entryId;

    String commenter;
    String content;

    Instant createdAt;
    @Nullable
    Instant updatedAt;

    public boolean isUpdated() {
        return updatedAt != null;
    }
}
