package com.andrew.weating.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.andrew.weating.entries.EntriesManager;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class CommentResource {

    private final EntriesManager entriesManager;

    @PostMapping(value = "{roomId}/comment", consumes = APPLICATION_JSON_VALUE)
    public void addComment(@PathVariable("roomId") String roomId, @RequestBody CommentAddBody addReq) {
        entriesManager.addComment(
                UUID.fromString(roomId),
                addReq.getSubmitter(),
                addReq.getPlaceId(),
                addReq.getCommenter(),
                addReq.getContent()
        );
    }

    @PutMapping(value = "{roomId}/comment", consumes = APPLICATION_JSON_VALUE)
    public void editComment(@PathVariable("roomId") String roomId, @RequestBody CommentEditBody editReq) {
        entriesManager.editComment(
                UUID.fromString(roomId),
                UUID.fromString(editReq.id),
                editReq.content
        );
    }

    @DeleteMapping(value = "{roomId}/comment")
    public void deleteComment(@PathVariable("roomId") String roomId, @RequestParam("id") String id) {
        entriesManager.deleteComment(UUID.fromString(roomId), UUID.fromString(id));
    }

    @Value
    static class CommentAddBody {
        String submitter;
        String placeId;
        String commenter;
        String content;
    }

    @Value
    static class CommentEditBody {
        String id;
        String content;
    }
}
