INSERT INTO roles (name)
SELECT 'ROLE_EMPLOYEE'
    WHERE NOT EXISTS (
    SELECT 1 FROM roles WHERE name = 'ROLE_EMPLOYEE'
);