ALTER TABLE loans
    ADD COLUMN library_item_id BIGINT;

ALTER TABLE loans
    ADD CONSTRAINT fk_loan_library_item
        FOREIGN KEY (library_item_id) REFERENCES library_items(id);