CREATE TABLE it_assets (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           asset_tag VARCHAR(100) UNIQUE,
                           serial_number VARCHAR(255),
                           purchase_date DATE,
                           purchase_cost NUMERIC(10, 2),
                           status VARCHAR(100) NOT NULL,

                           assigned_to_id BIGINT,

                           created_at TIMESTAMP NOT NULL,

                           CONSTRAINT fk_asset_assigned_to FOREIGN KEY (assigned_to_id) REFERENCES employees(id)
);

CREATE INDEX idx_asset_status ON it_assets(status);
CREATE INDEX idx_asset_assigned_to_id ON it_assets(assigned_to_id);