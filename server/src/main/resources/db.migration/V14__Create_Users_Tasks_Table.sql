CREATE TABLE users_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    task_id BIGINT,
    grade DECIMAL(10, 2),
    feedback VARCHAR(255),
    completed_at DATETIME,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (task_id) REFERENCES tasks(id)
);
