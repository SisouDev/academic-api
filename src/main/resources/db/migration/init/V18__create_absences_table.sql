CREATE TABLE absences (
                          id BIGSERIAL PRIMARY KEY,
                          type VARCHAR(100) NOT NULL,
                          date DATE NOT NULL,
                          justification TEXT,
                          attachment_url VARCHAR(255),
                          status VARCHAR(100) NOT NULL,

                          person_id BIGINT NOT NULL,
                          reviewed_by_id BIGINT,

                          created_at TIMESTAMP NOT NULL,
                          reviewed_at TIMESTAMP,

                          CONSTRAINT fk_absence_person FOREIGN KEY (person_id) REFERENCES people(id),
                          CONSTRAINT fk_absence_reviewed_by FOREIGN KEY (reviewed_by_id) REFERENCES people(id)
);

CREATE INDEX idx_absence_person_id ON absences(person_id);
CREATE INDEX idx_absence_status ON absences(status);