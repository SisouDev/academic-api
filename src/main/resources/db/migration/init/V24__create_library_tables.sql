CREATE TABLE library_items (
                               id BIGSERIAL PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               author VARCHAR(255),
                               isbn VARCHAR(50) UNIQUE,
                               publisher VARCHAR(255),
                               publication_year INT,
                               type VARCHAR(100) NOT NULL,
                               total_copies INT NOT NULL,
                               available_copies INT NOT NULL
);

CREATE TABLE loans (
                       id BIGSERIAL PRIMARY KEY,
                       item_id BIGINT NOT NULL,
                       borrower_id BIGINT NOT NULL,
                       loan_date DATE NOT NULL,
                       due_date DATE NOT NULL,
                       return_date DATE,
                       status VARCHAR(100) NOT NULL,

                       CONSTRAINT fk_loan_item FOREIGN KEY (item_id) REFERENCES library_items(id),
                       CONSTRAINT fk_loan_borrower FOREIGN KEY (borrower_id) REFERENCES people(id)
);

CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              item_id BIGINT NOT NULL,
                              person_id BIGINT NOT NULL,
                              reservation_date TIMESTAMP NOT NULL,
                              status VARCHAR(100) NOT NULL,

                              CONSTRAINT fk_reservation_item FOREIGN KEY (item_id) REFERENCES library_items(id),
                              CONSTRAINT fk_reservation_person FOREIGN KEY (person_id) REFERENCES people(id)
);

CREATE INDEX idx_library_item_title ON library_items(title);
CREATE INDEX idx_loan_borrower_id ON loans(borrower_id);
CREATE INDEX idx_loan_item_id ON loans(item_id);
CREATE INDEX idx_reservation_item_id ON reservations(item_id);