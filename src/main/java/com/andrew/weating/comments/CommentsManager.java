package com.andrew.weating.comments;

import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.UUID;

@RequiredArgsConstructor
public class CommentsManager {
    private final CommentsRepository commentsRepository;
    private final Clock clock;

    public void addComment(UUID room, String placeId, String commenter, String comment) {
        commentsRepository.add(new Comment(
                UUID.randomUUID(),
                room,
                placeId,
                commenter,
                comment,
                clock.instant(),
                null
        ));
    }

    public void editComment(UUID room, UUID id, String content) {
        commentsRepository.get(room, id)
                .ifPresent(comment -> commentsRepository.edit(comment, content, clock.instant()));
    }

    public void deleteComment(UUID room, UUID id) {
        commentsRepository.delete(room, id);
    }
}
