CREATE TABLE IF NOT EXISTS tokens (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      token_value VARCHAR(255) UNIQUE,
                                      token_type VARCHAR(50),
                                      revoked BOOLEAN,
                                      expired BOOLEAN,
                                      user_id BIGINT,
                                      FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS verification_tokens (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    token VARCHAR(255) NOT NULL,
                                    user_id BIGINT,
                                    type VARCHAR(255) NOT NULL,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    FOREIGN KEY (user_id) REFERENCES users(id)
);