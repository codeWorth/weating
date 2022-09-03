package com.andrew.weating.comments;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository {
    void add(Comment comment);
    void edit(Comment comment, String content, Instant updatedAt);
    void deleteAll(UUID room, Collection<UUID> ids);
    default void delete(UUID room, UUID id) {
        deleteAll(room, List.of(id));
    }

    Optional<Comment> get(UUID room, UUID id);
    Collection<Comment> getAll(UUID room, String placeId);
}
