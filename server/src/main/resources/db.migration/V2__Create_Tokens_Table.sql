CREATE TABLE tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE,
    tokenType ENUM('BEARER', 'OTHER_TOKEN_TYPE') NOT NULL,
    revoked BOOLEAN NOT NULL,
    expired BOOLEAN NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES _users(id)
);

