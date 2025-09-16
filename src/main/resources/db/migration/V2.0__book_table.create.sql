CREATE TABLE book
(
    id              BIGINT(20) AUTO_INCREMENT   NOT NULL,
    name            VARCHAR(255)                NOT NULL,
    is_available    BOOLEAN,
    author_id       BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_book_author_id FOREIGN KEY (author_id) REFERENCES author (id)
)