CREATE TABLE announcements (
                               id BIGSERIAL PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               content TEXT NOT NULL,
                               scope VARCHAR(100) NOT NULL,

                               created_by_id BIGINT NOT NULL,
                               department_id BIGINT,

                               created_at TIMESTAMP NOT NULL,
                               expires_at TIMESTAMP,

                               CONSTRAINT fk_announcement_created_by FOREIGN KEY (created_by_id) REFERENCES employees(id),
                               CONSTRAINT fk_announcement_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE INDEX idx_announcement_department_id ON announcements(department_id);