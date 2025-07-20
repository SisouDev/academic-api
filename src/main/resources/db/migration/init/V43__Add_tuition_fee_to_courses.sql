ALTER TABLE courses
    ADD COLUMN tuition_fee NUMERIC(10, 2);

UPDATE courses
SET tuition_fee = 850.00 WHERE tuition_fee IS NULL;

ALTER TABLE courses
    ALTER COLUMN tuition_fee SET NOT NULL;