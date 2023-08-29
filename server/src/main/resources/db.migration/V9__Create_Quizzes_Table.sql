CREATE TABLE questions
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    marks       DECIMAL(19, 2),
    description VARCHAR(255),
    quiz_id     BIGINT,
    is_deleted  TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes (id)
);


