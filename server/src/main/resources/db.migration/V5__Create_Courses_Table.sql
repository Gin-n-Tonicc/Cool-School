CREATE TABLE courses
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    a_class     VARCHAR(255),
    user_id     INT,
    category_id INT,
    is_deleted  TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES _users (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

