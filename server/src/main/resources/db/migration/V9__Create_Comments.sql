CREATE TABLE comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment VARCHAR(200) NOT NULL,
    created_at TIMESTAMP,
    user_id INT,
    blog_id BIGINT NOT NULL,
    deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (blog_id) REFERENCES blogs(id)
);

