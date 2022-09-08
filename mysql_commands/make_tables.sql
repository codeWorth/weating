DROP TABLE IF EXISTS entries;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS places;

CREATE TABLE entries (
    id CHAR(36) NOT NULL,
    room CHAR(36) NOT NULL,
    submitter VARCHAR(128) NOT NULL,
    created_at BIGINT NOT NULL,
    place_id TEXT NOT NULL,
    place_id_hash CHAR(44) NOT NULL,
    rating INT NOT NULL,
    review TEXT,
    PRIMARY KEY (room, submitter, place_id_hash)
);

CREATE TABLE comments (
    id CHAR(36) NOT NULL,
    place_id TEXT NOT NULL,
    place_id_hash CHAR(44) NOT NULL,
    room CHAR(36) NOT NULL,
    commenter VARCHAR(128) NOT NULL,
    content TEXT,
    created_at BIGINT NOT NULL,
    updated_at BIGINT,
    PRIMARY KEY (place_id_hash, room, id)
);

CREATE TABLE places (
    id TEXT NOT NULL,
    id_hash CHAR(44) NOT NULL PRIMARY KEY,
    refresh_at BIGINT NOT NULL,
    geometry TEXT NOT NULL
);