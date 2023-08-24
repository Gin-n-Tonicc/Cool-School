CREATE TABLE tasks
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    deadline      DATETIME,
    subsection_id BIGINT,
    is_deleted    TINYINT(1) DEFAULT FALSE,
    FOREIGN KEY (subsection_id) REFERENCES course_subsections (id)
);
