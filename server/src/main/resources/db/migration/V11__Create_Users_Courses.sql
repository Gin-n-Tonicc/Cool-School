CREATE TABLE users_courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    course_id BIGINT NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);