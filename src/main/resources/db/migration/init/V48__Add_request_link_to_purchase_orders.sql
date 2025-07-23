ALTER TABLE purchase_orders
    ADD COLUMN purchase_request_id BIGINT;

ALTER TABLE purchase_orders
    ADD CONSTRAINT uk_purchase_request_id UNIQUE (purchase_request_id);

ALTER TABLE purchase_orders
    ADD CONSTRAINT fk_po_purchase_request
        FOREIGN KEY (purchase_request_id) REFERENCES purchase_requests(id);