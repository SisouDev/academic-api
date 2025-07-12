CREATE TABLE leave_requests (
                                id BIGSERIAL PRIMARY KEY,
                                type VARCHAR(100) NOT NULL,
                                start_date DATE NOT NULL,
                                end_date DATE NOT NULL,
                                reason TEXT,
                                status VARCHAR(100) NOT NULL,

                                requester_id BIGINT NOT NULL,
                                reviewer_id BIGINT,

                                created_at TIMESTAMP NOT NULL,
                                reviewed_at TIMESTAMP,

                                CONSTRAINT fk_leave_requester FOREIGN KEY (requester_id) REFERENCES employees(id),
                                CONSTRAINT fk_leave_reviewer FOREIGN KEY (reviewer_id) REFERENCES employees(id)
);

CREATE INDEX idx_leave_request_requester_id ON leave_requests(requester_id);
CREATE INDEX idx_leave_request_status ON leave_requests(status);