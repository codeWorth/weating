package com.andrew.weating.entries;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommentsRepository {
    void add(Comment comment);
    void edit(Comment comment, String content, Instant updatedAt);

    Optional<Comment> get(UUID id);
    Collection<Comment> getAll(Collection<UUID> entryIds);
    default Collection<Comment> getAll(UUID entryId) {
        return getAll(List.of(entryId));
    }
}
