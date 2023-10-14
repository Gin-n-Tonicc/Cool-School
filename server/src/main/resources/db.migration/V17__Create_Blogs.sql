CREATE TABLE messages
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    content          VARCHAR(255) NOT NULL,
    summary          VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP,
    file_id    INT,
    owner_id INT,
    is_deleted       TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES files (id)
    FOREIGN KEY (file_id) REFERENCES files (id)
);

