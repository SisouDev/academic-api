ALTER TABLE support_tickets DROP CONSTRAINT IF EXISTS fk_ticket_assignee;

ALTER TABLE support_tickets ADD CONSTRAINT fk_ticket_assignee FOREIGN KEY (assignee_id) REFERENCES people(id);