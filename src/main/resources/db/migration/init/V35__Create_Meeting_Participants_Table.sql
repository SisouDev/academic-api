DROP TABLE IF EXISTS meeting_participants;

CREATE TABLE meeting_participants (
                                      id BIGSERIAL PRIMARY KEY,
                                      meeting_id BIGINT NOT NULL,
                                      person_id BIGINT NOT NULL,
                                      status VARCHAR(50) NOT NULL DEFAULT 'PENDING',

                                      CONSTRAINT fk_meeting_participants_on_meeting FOREIGN KEY (meeting_id) REFERENCES meetings(id),
                                      CONSTRAINT fk_meeting_participants_on_person FOREIGN KEY (person_id) REFERENCES people(id),
                                      UNIQUE (meeting_id, person_id)
);

CREATE INDEX idx_meeting_participants_on_meeting_id ON meeting_participants(meeting_id);
CREATE INDEX idx_meeting_participants_on_person_id ON meeting_participants(person_id);