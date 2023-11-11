CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    stars INT CHECK (stars >= 1 AND stars <= 5),
    text VARCHAR(50),
    is_deleted BOOLEAN NOT NULL
);

-- Add foreign key constraints
ALTER TABLE reviews
ADD CONSTRAINT fk_reviews_user FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE reviews
ADD CONSTRAINT fk_reviews_course FOREIGN KEY (course_id) REFERENCES courses(id);