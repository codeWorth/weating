package com.andrew.weating.comments;

import static generated.jooq.Tables.COMMENTS;
import static java.util.stream.Collectors.toList;

import com.andrew.weating.util.Hasher;
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
                .where(COMMENTS.PLACE_ID_HASH.eq(comment.getPlaceId())
                        .and(COMMENTS.ID.eq(comment.getId()))
                        .and(COMMENTS.COMMENTER.eq(comment.getContent())))
                .execute();
    }

    @Override
    public void deleteAll(UUID room, Collection<UUID> ids) {
        context.deleteFrom(COMMENTS)
                .where(COMMENTS.ROOM.eq(room)
                        .and(COMMENTS.ID.in(ids)))
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
    public Collection<Comment> getAll(UUID room, String placeId) {
        return context.selectFrom(COMMENTS)
                .where(COMMENTS.ROOM.eq(room)
                        .and(COMMENTS.PLACE_ID.eq(placeId)))
                .fetchStream()
                .map(this::fromRecord)
                .collect(toList());
    }

    private CommentsRecord toRecord(Comment comment) {
        return new CommentsRecord(
                comment.getId(),
                comment.getPlaceId(),
                Hasher.hash(comment.getPlaceId()),
                comment.getRoom(),
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
                record.getPlaceId(),
                record.getCommenter(),
                record.getContent(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
