CREATE TABLE _users
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    firstname  VARCHAR(255),
    lastname   VARCHAR(255),
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    address    VARCHAR(255),
    username   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    file_id    INT,
    provider   VARCHAR(255) NOT NULL DEFAULT `LOCAL`,
    is_additional_info_required TINYINT(1) NOT NULL DEFAULT FALSE,
    is_deleted TINYINT(1) NOT NULL DEFAULT FALSE,
    FOREIGN KEY (file_id) REFERENCES files (id)
);

