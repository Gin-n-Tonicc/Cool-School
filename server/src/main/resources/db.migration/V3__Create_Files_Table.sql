CREATE TABLE files
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    url        VARCHAR(255) NOT NULL,
    type       VARCHAR(255) NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE
);

