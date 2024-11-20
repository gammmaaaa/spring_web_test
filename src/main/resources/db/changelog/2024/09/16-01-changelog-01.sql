-- liquibase formatted sql

-- changeset e_cha:1726476397331-1
CREATE SEQUENCE IF NOT EXISTS ID_SEQ START WITH 1;

-- changeset e_cha:1726476397331-2
CREATE TABLE client
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

ALTER TABLE client
    ALTER COLUMN id SET DEFAULT nextval('ID_SEQ');

