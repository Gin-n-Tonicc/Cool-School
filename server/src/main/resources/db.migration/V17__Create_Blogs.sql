CREATE TABLE messages
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    content          VARCHAR(255) NOT NULL,
    summary          VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP,
    file_id    INT,
    owner_id INT,
    category_id INT,
    is_deleted       TINYINT(1) DEFAULT FALSE,
    is_enabled  TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users (id)
    FOREIGN KEY (file_id) REFERENCES files (id)
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

