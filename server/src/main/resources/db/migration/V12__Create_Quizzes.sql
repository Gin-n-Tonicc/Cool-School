CREATE TABLE quizzes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subsection_id BIGINT NOT NULL,
    attempt_limit INT,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (subsection_id) REFERENCES course_subsections(id)
);