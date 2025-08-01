CREATE TABLE job_history (
                             id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                             person_id BIGINT NOT NULL,
                             job_position VARCHAR(255) NOT NULL,
                             salary_structure_id BIGINT NOT NULL,
                             start_date DATE NOT NULL,
                             end_date DATE,
                             description TEXT,
                             CONSTRAINT fk_history_person FOREIGN KEY (person_id) REFERENCES people(id),
                             CONSTRAINT fk_history_salary_structure FOREIGN KEY (salary_structure_id) REFERENCES salary_structures(id)
);