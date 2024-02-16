CREATE TABLE quiz_attempts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    quiz_id BIGINT,
    user_id BIGINT,
    total_marks DECIMAL(10, 2),
    attempt_number INT,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
    FOREIGN KEY (user_id) REFERENCES _users(id)
);
