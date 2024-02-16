CREATE TABLE users_courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES _users(id),
    course_id BIGINT NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);