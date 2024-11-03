CREATE TABLE data_source_error_logs
(
    id               serial PRIMARY KEY,
    method_signature VARCHAR(100) NOT NULL,
    message          text,
    stacktrace       text
);