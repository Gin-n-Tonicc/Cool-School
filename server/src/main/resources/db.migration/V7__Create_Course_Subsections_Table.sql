CREATE TABLE course_subsections (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    course_id BIGINT,
    FOREIGN KEY (course_id) REFERENCES courses(id)
);
