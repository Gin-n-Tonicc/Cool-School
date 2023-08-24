CREATE TABLE user_quizzes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT,
    quiz_id     BIGINT,
    grade       DECIMAL(10, 2),
    feedback    TEXT,
    completedAt DATETIME,
    is_deleted  TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes (id)
);

