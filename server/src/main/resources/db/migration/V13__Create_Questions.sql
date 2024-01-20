CREATE TABLE questions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    marks DECIMAL NOT NULL,
    description VARCHAR(255) NOT NULL,
    quiz_id BIGINT NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);