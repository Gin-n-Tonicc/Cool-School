CREATE TABLE tokens
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    token     VARCHAR(255) UNIQUE,
    tokenType ENUM('ACCESS', 'REFRESH') NOT NULL,
    revoked   BOOLEAN NOT NULL,
    expired   BOOLEAN NOT NULL,
    user_id   BIGINT,
    FOREIGN KEY (user_id) REFERENCES _users(id)
);

