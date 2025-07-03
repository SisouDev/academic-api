CREATE TABLE course_sections (
                                 id BIGSERIAL PRIMARY KEY,
                                 name VARCHAR(255) NOT NULL,
                                 room VARCHAR(100),
                                 subject_id BIGINT NOT NULL,
                                 academic_term_id BIGINT NOT NULL,
                                 teacher_id BIGINT NOT NULL,
                                 CONSTRAINT fk_section_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
                                 CONSTRAINT fk_section_term FOREIGN KEY (academic_term_id) REFERENCES academic_terms(id),
                                 CONSTRAINT fk_section_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);