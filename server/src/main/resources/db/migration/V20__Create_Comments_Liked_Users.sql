CREATE TABLE comments_liked_users (
    comment_id BIGINT,
    liked_users_id BIGINT,
    PRIMARY KEY (comment_id, liked_users_id),
    FOREIGN KEY (comment_id) REFERENCES comments(id),
    FOREIGN KEY (liked_users_id) REFERENCES _users(id)
);