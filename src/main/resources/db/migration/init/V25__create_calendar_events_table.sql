CREATE TABLE calendar_events (
                                 id BIGSERIAL PRIMARY KEY,
                                 title VARCHAR(255) NOT NULL,
                                 description TEXT,
                                 type VARCHAR(100) NOT NULL,
                                 start_time TIMESTAMP NOT NULL,
                                 end_time TIMESTAMP NOT NULL,
                                 scope VARCHAR(100) NOT NULL,

                                 department_id BIGINT,

                                 source_entity_id BIGINT,

                                 created_at TIMESTAMP NOT NULL,

                                 CONSTRAINT fk_calendar_event_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE INDEX idx_calendar_event_times ON calendar_events(start_time, end_time);
CREATE INDEX idx_calendar_event_scope ON calendar_events(scope, department_id);