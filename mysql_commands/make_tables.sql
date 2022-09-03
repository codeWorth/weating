DROP TABLE IF EXISTS entries;

CREATE TABLE entries (
    room CHAR(12) NOT NULL,
    submitter VARCHAR(128) NOT NULL,
    created_at BIGINT NOT NULL,
    place_id TEXT NOT NULL,
    place_id_hash CHAR(44) NOT NULL,
    place_id_refresh_at BIGINT NOT NULL,
    rating INT NOT NULL,
    review TEXT,
    PRIMARY KEY (room, submitter, place_id_hash)
);