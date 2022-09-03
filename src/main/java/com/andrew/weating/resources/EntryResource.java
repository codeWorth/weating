package com.andrew.weating.resources;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.andrew.weating.entries.EntriesManager;
import com.andrew.weating.entries.Entry;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
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
public class EntryResource {

    private final EntriesManager entriesManager;

    @PostMapping(value = "{roomId}/entry", consumes = APPLICATION_JSON_VALUE)
    public void addEntry(@PathVariable("roomId") String roomId, @RequestBody EntryAddBody addReq) {
        entriesManager.addEntry(
                UUID.fromString(roomId),
                addReq.getSubmitter(),
                addReq.getPlaceId(),
                addReq.getRating(),
                addReq.getReview()
        );
    }

    @PutMapping(value = "{roomId}/entry", consumes = APPLICATION_JSON_VALUE)
    public void updateEntry(@PathVariable("roomId") String roomId, @RequestBody EntryUpdateBody updateReq) {
        entriesManager.updateEntry(
                UUID.fromString(roomId),
                updateReq.getSubmitter(),
                updateReq.getPlaceId(),
                entry -> updateReq.getChanges().updateEntry(entry)
        );
    }

    @DeleteMapping(value = "{roomId}/entry")
    public void deleteEntry(
            @PathVariable("roomId") String roomId,
            @RequestParam("submitter") String submitter,
            @RequestParam("placeId") String placeId
    ) {
        entriesManager.deleteEntry(UUID.fromString(roomId), submitter, placeId);
    }

    @Value
    static class EntryAddBody {
        String submitter;
        String placeId;
        double rating;
        String review;
    }

    @Value
    static class EntryUpdateBody {
        String submitter;
        String placeId;
        Changes changes;

        @Data
        static class Changes {
            Double rating = null;
            String review = null;

            public Entry updateEntry(Entry entry) {
                return entry
                        .withRating(rating == null ? entry.getRating() : rating)
                        .withReview(review == null ? entry.getReview() : review);
            }
        }
    }
}
