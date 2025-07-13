ALTER TABLE announcements
    ADD COLUMN course_section_id BIGINT,
    ADD CONSTRAINT fk_announcements_on_course_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id);