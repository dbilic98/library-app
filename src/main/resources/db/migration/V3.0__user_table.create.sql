CREATE TABLE users
(
    id       BIGINT(20) AUTO_INCREMENT NOT NULL,
    username VARCHAR(255)              NOT NULL,
    password VARCHAR(255)              NOT NULL,
    PRIMARY KEY (id)
)