CREATE TABLE financial_transactions (
                                        id BIGSERIAL PRIMARY KEY,
                                        student_id BIGINT NOT NULL,
                                        description VARCHAR(255) NOT NULL,
                                        amount NUMERIC(10, 2) NOT NULL,
                                        type VARCHAR(100) NOT NULL,
                                        transaction_date DATE NOT NULL,
                                        created_at TIMESTAMP NOT NULL,

                                        CONSTRAINT fk_transaction_student FOREIGN KEY (student_id) REFERENCES students(id)
);

CREATE INDEX idx_financial_transaction_student_id ON financial_transactions(student_id);