CREATE TABLE lesson_plans (
                              id BIGSERIAL PRIMARY KEY,
                              objectives TEXT,
                              syllabus_content TEXT,
                              bibliography TEXT,
                              course_section_id BIGINT NOT NULL UNIQUE,
                              CONSTRAINT fk_lesson_plan_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id)
);

CREATE TABLE enrollments (
                             id BIGSERIAL PRIMARY KEY,
                             enrollment_date DATE NOT NULL,
                             total_absences INT DEFAULT 0,
                             status VARCHAR(50) NOT NULL,
                             course_section_id BIGINT NOT NULL,
                             student_id BIGINT NOT NULL,
                             CONSTRAINT fk_enrollment_section FOREIGN KEY (course_section_id) REFERENCES course_sections(id),
                             CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE TABLE assessments (
                             id BIGSERIAL PRIMARY KEY,
                             score NUMERIC(5, 2),
                             assessment_date DATE NOT NULL,
                             type VARCHAR(100) NOT NULL,
                             enrollment_id BIGINT NOT NULL,

                             assessment_definition_id BIGINT NOT NULL,

                             CONSTRAINT fk_assessment_enrollment
                                 FOREIGN KEY (enrollment_id) REFERENCES enrollments(id),
                             CONSTRAINT fk_assessment_to_definition
                                 FOREIGN KEY (assessment_definition_id) REFERENCES assessment_definitions(id)
);



CREATE TABLE attendance_records (
                                    id BIGSERIAL PRIMARY KEY,
                                    date DATE NOT NULL,
                                    was_present BOOLEAN NOT NULL,
                                    enrollment_id BIGINT NOT NULL,
                                    CONSTRAINT fk_attendance_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id)
);