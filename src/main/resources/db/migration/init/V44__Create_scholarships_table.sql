CREATE TABLE scholarships (
                              id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                              enrollment_id BIGINT NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              discount_type VARCHAR(255) NOT NULL,
                              value NUMERIC(10, 2) NOT NULL,
                              status VARCHAR(255) NOT NULL,
                              start_date DATE,
                              end_date DATE,
                              CONSTRAINT fk_scholarship_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id)
);