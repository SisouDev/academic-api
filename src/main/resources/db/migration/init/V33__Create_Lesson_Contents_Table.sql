CREATE TABLE lesson_contents (
                                 id BIGSERIAL PRIMARY KEY,

                                 lesson_id BIGINT NOT NULL,

                                 type VARCHAR(50) NOT NULL,

                                 content TEXT NOT NULL,

                                 display_order INT NOT NULL,

                                 CONSTRAINT fk_lesson_contents_on_lesson FOREIGN KEY (lesson_id) REFERENCES lessons(id)
);

CREATE INDEX idx_lesson_contents_on_lesson_id ON lesson_contents(lesson_id);
