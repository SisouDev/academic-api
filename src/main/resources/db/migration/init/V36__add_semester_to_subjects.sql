ALTER TABLE subjects
    ADD COLUMN semester INTEGER;


UPDATE subjects
SET semester = 1 WHERE semester IS NULL;

ALTER TABLE subjects
    ALTER COLUMN semester SET NOT NULL;