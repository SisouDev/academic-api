CREATE TABLE activity_logs (
                               id BIGSERIAL PRIMARY KEY,
                               user_id BIGINT,
                               description VARCHAR(255) NOT NULL,
                               timestamp TIMESTAMP NOT NULL,
                               CONSTRAINT fk_activity_log_user FOREIGN KEY (user_id) REFERENCES users(id)
);