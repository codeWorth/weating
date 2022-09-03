package com.andrew.weating.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.andrew.weating.entries.Comment;
import com.andrew.weating.entries.EntriesManager;
import com.andrew.weating.entries.Entry;
import com.andrew.weating.util.Lists;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class EntriesResource {

    private final EntriesManager entriesManager;

    @GetMapping(value = "{roomId}/entries", produces = APPLICATION_JSON_VALUE)
    public Collection<EntryView> entries(@PathVariable("roomId") String roomId) {
        return Lists.map(entriesManager.getEntriesForRoom(UUID.fromString(roomId)), EntryView::new);
    }

    @GetMapping(value = "{roomId}/{placeId}/entries", produces = APPLICATION_JSON_VALUE)
    public Collection<FullEntryView> entriesAtPlace(
            @PathVariable("roomId") String roomId,
            @PathVariable("placeId") String placeId
    ) {
        return Lists.map(entriesManager.getEntriesForPlace(UUID.fromString(roomId), placeId),
                entry -> new FullEntryView(
                        entry.getEntry(),
                        Lists.map(entry.getComments(), CommentView::new)
                ));
    }

    @RequiredArgsConstructor
    static class EntryView {
        @Delegate(types = EntryView.Properties.class)
        private final Entry entry;

        interface Properties {
            String getSubmitter();
            double getRating();
            String getReview();
            Instant getCreatedAt();
            String getPlaceId();
        }
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

    @RequiredArgsConstructor
    static class FullEntryView {
        @Delegate(types = EntryView.Properties.class)
        private final Entry entry;
        private final Collection<CommentView> comments;
    }
}
