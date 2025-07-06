CREATE TABLE notifications (
                               id BIGSERIAL PRIMARY KEY,

                               recipient_user_id BIGINT NOT NULL,

                               message VARCHAR(255) NOT NULL,

                               type VARCHAR(50) NOT NULL,

                               status VARCHAR(50) NOT NULL DEFAULT 'UNREAD',

                               link VARCHAR(255),

                               created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,

                               CONSTRAINT fk_notification_to_user
                                   FOREIGN KEY (recipient_user_id)
                                       REFERENCES users(id)
                                       ON DELETE CASCADE
);