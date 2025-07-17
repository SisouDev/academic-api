ALTER TABLE financial_transactions
    ADD COLUMN status VARCHAR(255);

UPDATE financial_transactions
SET status = 'PAID' WHERE status IS NULL;

ALTER TABLE financial_transactions
    ALTER COLUMN status SET NOT NULL;