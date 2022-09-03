package com.andrew.weating.entries;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository {
    void add(Comment comment);
    void edit(Comment comment, String content, Instant updatedAt);
    void delete(UUID room, UUID id);

    Optional<Comment> get(UUID room, UUID id);
    Collection<Comment> getAll(UUID room, Collection<UUID> entryIds);
    default Collection<Comment> getAll(UUID room, UUID entryId) {
        return getAll(room, List.of(entryId));
    }
}
