INSERT INTO roles (name)
SELECT 'ROLE_TEACHER'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_TEACHER'
);

INSERT INTO roles (name)
SELECT 'ROLE_STUDENT'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_STUDENT'
);