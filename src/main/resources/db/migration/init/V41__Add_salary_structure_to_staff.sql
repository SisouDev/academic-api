ALTER TABLE employees
    ADD COLUMN salary_structure_id BIGINT;

ALTER TABLE teachers
    ADD COLUMN salary_structure_id BIGINT;

ALTER TABLE employees
    ADD CONSTRAINT fk_employee_salary_structure
        FOREIGN KEY (salary_structure_id) REFERENCES salary_structures(id);

ALTER TABLE teachers
    ADD CONSTRAINT fk_teacher_salary_structure
        FOREIGN KEY (salary_structure_id) REFERENCES salary_structures(id);