CREATE TABLE messages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    receiver_id BIGINT,
    sent_at TIMESTAMP,
    content VARCHAR(255) NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (receiver_id) REFERENCES _users(id)
);
