CREATE TABLE blogs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    content MEDIUMTEXT NOT NULL,
    summary VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    file_id BIGINT,
    user_id BIGINT,
    category_id BIGINT,
    is_deleted TINYINT(1) DEFAULT FALSE,
    is_enabled TINYINT(1) DEFAULT FALSE,
    comment_count INT DEFAULT 0,
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

