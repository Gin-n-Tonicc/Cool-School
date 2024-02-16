CREATE TABLE _users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    address VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    description VARCHAR(120) NOT NULL,
    role VARCHAR(255) NOT NULL,
    file_id BIGINT,
    provider VARCHAR(255) NOT NULL,
    is_additional_info_required TINYINT(1) NOT NULL DEFAULT FALSE,
    is_deleted TINYINT(1) NOT NULL DEFAULT FALSE,
    enabled TINYINT(1) NOT NULL DEFAULT FALSE,
    FOREIGN KEY (file_id) REFERENCES files(id)
);

