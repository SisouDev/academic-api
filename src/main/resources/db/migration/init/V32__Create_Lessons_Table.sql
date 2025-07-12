CREATE TABLE lessons (
                         id BIGSERIAL PRIMARY KEY,

                         course_section_id BIGINT NOT NULL,

                         topic VARCHAR(255) NOT NULL,

                         description TEXT,

                         lesson_date DATE NOT NULL,

                         created_at TIMESTAMP NOT NULL,

                         CONSTRAINT fk_lessons_on_course_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id)
);
CREATE INDEX idx_lessons_on_course_section_id ON lessons(course_section_id);
