CREATE TABLE user_answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT,
    answer_id BIGINT,
    quiz_attempt_id BIGINT,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (answer_id) REFERENCES answers(id),
    FOREIGN KEY (quiz_attempt_id) REFERENCES quiz_attempts(id)
);
