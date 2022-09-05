package com.andrew.weating.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.andrew.weating.comments.Comment;
import com.andrew.weating.comments.CommentsManager;
import com.andrew.weating.util.Lists;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Delegate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CommentResource {
    private final CommentsManager commentsManager;

    @GetMapping(value = "{roomId}/comments", produces = APPLICATION_JSON_VALUE)
    public Collection<CommentView> getCommentsForPlace(
            @PathVariable("roomId") String roomId,
            @RequestParam("placeId") String placeId
    ) {
        return Lists.map(commentsManager.getAllForPlace(UUID.fromString(roomId), placeId), CommentView::new);
    }

    @PostMapping(value = "{roomId}/comment", consumes = APPLICATION_JSON_VALUE)
    public void addComment(@PathVariable("roomId") String roomId, @RequestBody CommentAddBody addReq) {
        commentsManager.addComment(
                UUID.fromString(roomId),
                addReq.getPlaceId(),
                addReq.getCommenter(),
                addReq.getContent()
        );
    }

    @PutMapping(value = "{roomId}/comment", consumes = APPLICATION_JSON_VALUE)
    public void editComment(@PathVariable("roomId") String roomId, @RequestBody CommentEditBody editReq) {
        commentsManager.editComment(
                UUID.fromString(roomId),
                UUID.fromString(editReq.getId()),
                editReq.getContent()
        );
    }

    @DeleteMapping(value = "{roomId}/comment")
    public void deleteComment(@PathVariable("roomId") String roomId, @RequestParam("id") String id) {
        commentsManager.deleteComment(UUID.fromString(roomId), UUID.fromString(id));
    }

    @Value
    static class CommentAddBody {
        String placeId;
        String commenter;
        String content;
    }

    @Value
    static class CommentEditBody {
        String id;
        String content;
    }

    @RequiredArgsConstructor
    static class CommentView {
        @Delegate(types = CommentView.Properties.class)
        private final Comment comment;

        interface Properties {
            UUID getId();
            String getCommenter();
            String getContent();
            Instant getCreatedAt();
            Instant getUpdatedAt();
            boolean isUpdated();
        }
    }
}
