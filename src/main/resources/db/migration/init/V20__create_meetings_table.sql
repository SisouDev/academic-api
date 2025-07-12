CREATE TABLE meetings (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          description TEXT,
                          start_time TIMESTAMP NOT NULL,
                          end_time TIMESTAMP NOT NULL,
                          visibility VARCHAR(100) NOT NULL,
                          organizer_id BIGINT NOT NULL,

                          created_at TIMESTAMP NOT NULL,

                          CONSTRAINT fk_meeting_organizer FOREIGN KEY (organizer_id) REFERENCES people(id)
);

CREATE TABLE meeting_participants (
                                      meeting_id BIGINT NOT NULL,
                                      person_id BIGINT NOT NULL,

                                      PRIMARY KEY (meeting_id, person_id),

                                      CONSTRAINT fk_participant_meeting FOREIGN KEY (meeting_id) REFERENCES meetings(id) ON DELETE CASCADE,
                                      CONSTRAINT fk_participant_person FOREIGN KEY (person_id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE INDEX idx_meeting_organizer_id ON meetings(organizer_id);
CREATE INDEX idx_meeting_start_time ON meetings(start_time);