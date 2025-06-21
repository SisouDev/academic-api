CREATE TABLE departments (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(255) NOT NULL,
                             acronym VARCHAR(20),
                             institution_id BIGINT NOT NULL,
                             CONSTRAINT fk_department_institution FOREIGN KEY (institution_id) REFERENCES institutions(id)
);

CREATE TABLE courses (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         duration_in_semesters INT,
                         department_id BIGINT,
                         CONSTRAINT fk_course_department FOREIGN KEY (department_id) REFERENCES departments(id)
);

CREATE TABLE subjects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          workload_hours INT,
                          course_id BIGINT,
                          CONSTRAINT fk_subject_course FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE academic_terms (
                                id BIGSERIAL PRIMARY KEY,
                                year INT NOT NULL,
                                semester INT NOT NULL,
                                start_date DATE NOT NULL,
                                end_date DATE NOT NULL,
                                status VARCHAR(50) NOT NULL,
                                institution_id BIGINT NOT NULL,
                                CONSTRAINT fk_academic_term_institution FOREIGN KEY (institution_id) REFERENCES institutions(id)
);