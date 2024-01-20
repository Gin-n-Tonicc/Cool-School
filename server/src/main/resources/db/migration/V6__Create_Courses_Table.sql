CREATE TABLE courses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    objectives MEDIUMTEXT NOT NULL,
    eligibility MEDIUMTEXT NOT NULL,
    user_id BIGINT,
    file_id BIGINT,
    category_id BIGINT,
    stars DECIMAL(10, 2),
    created_date TIMESTAMP NOT NULL,
    is_deleted TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES _users(id),
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
)

