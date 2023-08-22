CREATE TABLE users_courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    course_id BIGINT NOT NULL,
    UNIQUE(user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);