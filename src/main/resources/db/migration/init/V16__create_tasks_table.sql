CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       due_date DATE,
                       status VARCHAR(100) NOT NULL,

                       assignee_id BIGINT,
                       department_id BIGINT NOT NULL,
                       created_by_id BIGINT NOT NULL,

                       created_at TIMESTAMP NOT NULL,
                       completed_at TIMESTAMP,


                       CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES employees(id),
                       CONSTRAINT fk_task_department FOREIGN KEY (department_id) REFERENCES departments(id),
                       CONSTRAINT fk_task_created_by FOREIGN KEY (created_by_id) REFERENCES employees(id)
);

CREATE INDEX idx_task_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_task_department_id ON tasks(department_id);