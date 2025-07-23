CREATE TABLE institutions (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              register_id VARCHAR(255) NOT NULL UNIQUE,
                              created_at TIMESTAMP NOT NULL,


                              street VARCHAR(255),
                              "number" VARCHAR(50),
                              complement VARCHAR(255),
                              district VARCHAR(100),
                              city VARCHAR(100),
                              state VARCHAR(50),
                              zip_code VARCHAR(20)
);

CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (name) VALUES
                             ('ROLE_ADMIN'),
                             ('ROLE_USER'),
                             ('ROLE_FINANCE_MANAGER'),
                             ('ROLE_MANAGER');