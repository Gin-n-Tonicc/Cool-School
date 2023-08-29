CREATE TABLE categories
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE
);

