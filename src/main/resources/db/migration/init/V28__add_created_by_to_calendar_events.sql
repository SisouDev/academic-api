ALTER TABLE calendar_events
    ADD COLUMN created_by_id BIGINT;

ALTER TABLE calendar_events
    ADD CONSTRAINT fk_event_created_by
        FOREIGN KEY (created_by_id) REFERENCES employees(id);