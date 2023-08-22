CREATE TABLE users_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    answer_id BIGINT,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (answer_id) REFERENCES answers(id)
);

