CREATE TABLE answers
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    text        VARCHAR(255),
    question_id BIGINT,
    isCorrect   TINYINT(1),
    is_deleted  TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (question_id) REFERENCES questions (id)
);
