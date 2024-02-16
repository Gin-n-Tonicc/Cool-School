CREATE TABLE user_quizzes_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id BIGINT,
    answer_id BIGINT,
    quiz_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (question_id) REFERENCES questions(id),
    FOREIGN KEY (answer_id) REFERENCES answers(id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id),
    FOREIGN KEY (user_id) REFERENCES _users(id)
);
