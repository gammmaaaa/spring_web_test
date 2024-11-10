CREATE TABLE data_source_error_logs
(
    id               BIGINT,
    method_signature VARCHAR(100) NOT NULL,
    message          text,
    stacktrace       text,
    CONSTRAINT pk_data_source_error_logs_id PRIMARY KEY (id)
);

ALTER TABLE data_source_error_logs
    ALTER COLUMN id SET DEFAULT nextval('ID_SEQ');
-- CREATE SEQUENCE IF NOT EXISTS DATA_SOURCE_ERROR_LOGS_ID_SEQ START WITH 1 owned by data_source_error_logs.id;
-- ALTER TABLE data_source_error_logs ALTER COLUMN id SET DEFAULT nextval('DATA_SOURCE_ERROR_LOGS_ID_SEQ');