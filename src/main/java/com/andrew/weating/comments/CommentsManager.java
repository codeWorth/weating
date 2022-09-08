package com.andrew.weating.comments;

import com.andrew.weating.util.Collections;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
public class CommentsManager {
    private final CommentsRepository commentsRepository;
    private final Clock clock;

    public Collection<Comment> getAllForPlace(UUID room, String placeId) {
        return commentsRepository.getAll(room, placeId);
    }

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

    public void deleteComments(UUID room, String placeId) {
        commentsRepository.deleteAll(room, Collections.map(commentsRepository.getAll(room, placeId), Comment::getId));
    }
}
