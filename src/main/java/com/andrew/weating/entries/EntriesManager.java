package com.andrew.weating.entries;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.andrew.weating.util.Lists;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
public class EntriesManager {
    private final EntriesRepository entriesRepository;
    private final CommentsRepository commentsRepository;
    private final Clock clock;

    public void addEntry(UUID room, String submitter, String placeId, double rating, String review) {
        entriesRepository.add(new Entry(
                UUID.randomUUID(),
                room,
                submitter,
                rating,
                review,
                clock.instant(),
                placeId,
                clock.instant()
        ));
    }

    public void updateEntry(UUID room, String submitter, String placeId, Function<Entry, Entry> updateFunc) {
        entriesRepository.get(room, submitter, placeId)
                .map(updateFunc)
                .ifPresent(entriesRepository::add);
    }

    public void deleteEntry(UUID room, String submitter, String placeId) {
        entriesRepository.delete(room, submitter, placeId);
    }

    public Collection<Entry> getEntriesForRoom(UUID room) {
        return entriesRepository.getAll(room);
    }

    public Collection<FullEntry> getEntriesForPlace(UUID room, String placeId) {
        List<Entry> entries = entriesRepository.getAll(room).stream()
                .filter(entry -> entry.getPlaceId().equals(placeId))
                .collect(toList());

        Map<UUID, List<Comment>> comments = commentsRepository.getAll(room,
                        entries.stream()
                                .map(Entry::getId)
                                .collect(toList()))
                .stream()
                .collect(toMap(Comment::getEntryId, List::of, Lists::merge));

        return entries.stream()
                .map(entry -> new FullEntry(
                        entry,
                        comments.get(entry.getId())
                ))
                .collect(toList());
    }

    public void addComment(UUID room, String submitter, String placeId, String commenter, String comment) {
        entriesRepository.get(room, submitter, placeId)
                .map(entry -> new Comment(
                        UUID.randomUUID(),
                        room,
                        entry.getId(),
                        commenter,
                        comment,
                        clock.instant(),
                        null
                ))
                .ifPresent(commentsRepository::add);
    }

    public void editComment(UUID room, UUID id, String content) {
        commentsRepository.get(room, id)
                .ifPresent(comment -> commentsRepository.edit(comment, content, clock.instant()));
    }

    public void deleteComment(UUID room, UUID id) {
        commentsRepository.delete(room, id);
    }
}
