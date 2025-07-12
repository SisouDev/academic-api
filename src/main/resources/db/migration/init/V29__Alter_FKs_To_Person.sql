ALTER TABLE tasks DROP CONSTRAINT IF EXISTS fk_task_assignee;
ALTER TABLE tasks ADD CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES people(id);

ALTER TABLE calendar_events DROP CONSTRAINT IF EXISTS fk_event_created_by;
ALTER TABLE calendar_events ADD CONSTRAINT fk_event_created_by FOREIGN KEY (created_by_id) REFERENCES people(id);

ALTER TABLE announcements DROP CONSTRAINT IF EXISTS fk_announcement_created_by;
ALTER TABLE announcements ADD CONSTRAINT fk_announcement_created_by FOREIGN KEY (created_by_id) REFERENCES people(id);