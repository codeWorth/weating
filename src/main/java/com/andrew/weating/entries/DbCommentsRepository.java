package com.andrew.weating.entries;

import static generated.jooq.Tables.COMMENTS;
import static java.util.stream.Collectors.toList;

import generated.jooq.tables.records.CommentsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class DbCommentsRepository implements CommentsRepository {
    private final DSLContext context;

    @Override
    public void add(Comment comment) {
        context.insertInto(COMMENTS)
                .set(toRecord(comment))
                .onDuplicateKeyIgnore()
                .execute();
    }

    @Override
    public void edit(Comment comment, String content, Instant updatedAt) {
        context.update(COMMENTS)
                .set(COMMENTS.CONTENT, content)
                .set(COMMENTS.UPDATED_AT, updatedAt)
                .where(COMMENTS.ENTRY_ID.eq(comment.getEntryId())
                        .and(COMMENTS.ID.eq(comment.getId()))
                        .and(COMMENTS.COMMENTER.eq(comment.getContent())))
                .execute();
    }

    @Override
    public void delete(UUID room, UUID id) {
        context.deleteFrom(COMMENTS)
                .where(COMMENTS.ROOM.eq(room)
                        .and(COMMENTS.ID.eq(id)))
                .execute();
    }

    @Override
    public Optional<Comment> get(UUID room, UUID id) {
        return context.selectFrom(COMMENTS)
                .where(COMMENTS.ROOM.eq(room)
                        .and(COMMENTS.ID.eq(id)))
                .fetchOptional()
                .map(this::fromRecord);
    }

    @Override
    public Collection<Comment> getAll(UUID room, Collection<UUID> entryIds) {
        return context.selectFrom(COMMENTS)
                .where(COMMENTS.ROOM.eq(room)
                        .and(COMMENTS.ID.in(entryIds)))
                .fetchStream()
                .map(this::fromRecord)
                .collect(toList());
    }

    private CommentsRecord toRecord(Comment comment) {
        return new CommentsRecord(
                comment.getId(),
                comment.getRoom(),
                comment.getEntryId(),
                comment.getCommenter(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }

    private Comment fromRecord(CommentsRecord record) {
        return new Comment(
                record.getId(),
                record.getRoom(),
                record.getEntryId(),
                record.getCommenter(),
                record.getContent(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
