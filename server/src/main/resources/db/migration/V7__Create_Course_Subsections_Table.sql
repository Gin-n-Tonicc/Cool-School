CREATE TABLE course_subsections (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    course_id BIGINT,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);
