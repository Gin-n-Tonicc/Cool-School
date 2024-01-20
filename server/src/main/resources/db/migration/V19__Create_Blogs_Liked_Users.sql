CREATE TABLE blogs_liked_users (
    blog_id BIGINT,
    liked_users_id BIGINT,
    PRIMARY KEY (blog_id, liked_users_id),
    FOREIGN KEY (blog_id) REFERENCES blogs(id),
    FOREIGN KEY (liked_users_id) REFERENCES _users(id)
);