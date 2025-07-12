CREATE TABLE materials (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL UNIQUE,
                           description TEXT,
                           type VARCHAR(100) NOT NULL
);

CREATE TABLE internal_requests (
                                   id BIGSERIAL PRIMARY KEY,
                                   title VARCHAR(255) NOT NULL,
                                   description TEXT,
                                   type VARCHAR(100) NOT NULL,
                                   status VARCHAR(100) NOT NULL,
                                   urgency VARCHAR(100) NOT NULL,


                                   requester_id BIGINT NOT NULL,
                                   target_department_id BIGINT,
                                   handler_id BIGINT,

                                   created_at TIMESTAMP NOT NULL,
                                   resolved_at TIMESTAMP,
                                   resolution_notes TEXT,

                                   CONSTRAINT fk_request_requester FOREIGN KEY (requester_id) REFERENCES people(id),
                                   CONSTRAINT fk_request_department FOREIGN KEY (target_department_id) REFERENCES departments(id),
                                   CONSTRAINT fk_request_handler FOREIGN KEY (handler_id) REFERENCES people(id)
);

CREATE INDEX idx_requester_id ON internal_requests(requester_id);
CREATE INDEX idx_target_department_id ON internal_requests(target_department_id);