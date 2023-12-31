CREATE TABLE messages
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_user_id   BIGINT,
    receiver_user_id BIGINT,
    is_group_chat      TINYINT(1),
    sent_at          TIMESTAMP,
    content          VARCHAR(255) NOT NULL,
    is_deleted       TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (sender_user_id) REFERENCES users (id),
    FOREIGN KEY (receiver_user_id) REFERENCES users (id)
);

