CREATE TABLE quizzes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    timeLimit BIGINT,
    subsection_id BIGINT,
    FOREIGN KEY (subsection_id) REFERENCES course_subsections(id)
);

