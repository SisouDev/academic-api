CREATE TABLE people (
                        id BIGSERIAL PRIMARY KEY,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        phone VARCHAR(20),
                        status VARCHAR(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        institution_id BIGINT NOT NULL,


                        "type" VARCHAR(50),
                        "number" VARCHAR(50),

                        CONSTRAINT fk_person_institution FOREIGN KEY (institution_id) REFERENCES institutions(id)
);


CREATE TABLE students (
                          id BIGINT PRIMARY KEY,
                          birth_date DATE,


                          street VARCHAR(255),
                          "number" VARCHAR(50),
                          complement VARCHAR(255),
                          district VARCHAR(100),
                          city VARCHAR(100),
                          state VARCHAR(50),
                          zip_code VARCHAR(20),

                          CONSTRAINT fk_student_person FOREIGN KEY (id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE teachers (
                          id BIGINT PRIMARY KEY,
                          academic_background VARCHAR(100),
                          CONSTRAINT fk_teacher_person FOREIGN KEY (id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE employees (
                           id BIGINT PRIMARY KEY,
                           job_position VARCHAR(100),
                           hiring_date DATE,
                           CONSTRAINT fk_employee_person FOREIGN KEY (id) REFERENCES people(id) ON DELETE CASCADE
);

CREATE TABLE institution_admins (
                                    id BIGINT PRIMARY KEY,
                                    CONSTRAINT fk_admin_person FOREIGN KEY (id) REFERENCES people(id) ON DELETE CASCADE
);


CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       person_id BIGINT NOT NULL UNIQUE,
                       CONSTRAINT fk_user_person FOREIGN KEY (person_id) REFERENCES people(id) ON DELETE CASCADE
);


CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);