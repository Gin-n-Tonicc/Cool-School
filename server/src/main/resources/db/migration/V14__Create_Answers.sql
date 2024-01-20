CREATE TABLE answers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(255) NOT NULL,
    question_id BIGINT NOT NULL,
    is_correct TINYINT(1) NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (question_id) REFERENCES questions(id)
);