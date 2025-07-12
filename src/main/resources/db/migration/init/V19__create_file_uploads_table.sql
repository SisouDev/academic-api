CREATE TABLE file_uploads (
                              id BIGSERIAL PRIMARY KEY,
                              original_file_name VARCHAR(255) NOT NULL,
                              stored_file_name VARCHAR(255) NOT NULL UNIQUE,
                              file_type VARCHAR(100),
                              file_size BIGINT NOT NULL,
                              storage_path VARCHAR(255) NOT NULL,

                              reference_id BIGINT NOT NULL,
                              reference_type VARCHAR(100) NOT NULL,

                              created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_file_uploads_reference ON file_uploads(reference_id, reference_type);