CREATE TABLE messages
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    content          VARCHAR(255) NOT NULL,
    created_at          TIMESTAMP,
    owner_id INT,
    blog_id INT,
    is_deleted       TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users (id)
    FOREIGN KEY (blog_id) REFERENCES blogs (id)
);

