CREATE TABLE support_tickets (
                                 id BIGSERIAL PRIMARY KEY,
                                 title VARCHAR(255) NOT NULL,
                                 description TEXT NOT NULL,
                                 status VARCHAR(100) NOT NULL,
                                 priority VARCHAR(100) NOT NULL,
                                 category VARCHAR(100) NOT NULL,

                                 requester_id BIGINT NOT NULL,
                                 assignee_id BIGINT,

                                 created_at TIMESTAMP NOT NULL,
                                 resolved_at TIMESTAMP,
                                 closed_at TIMESTAMP,

                                 CONSTRAINT fk_ticket_requester FOREIGN KEY (requester_id) REFERENCES people(id),
                                 CONSTRAINT fk_ticket_assignee FOREIGN KEY (assignee_id) REFERENCES employees(id)
);

CREATE INDEX idx_ticket_requester_id ON support_tickets(requester_id);
CREATE INDEX idx_ticket_assignee_id ON support_tickets(assignee_id);
CREATE INDEX idx_ticket_status ON support_tickets(status);