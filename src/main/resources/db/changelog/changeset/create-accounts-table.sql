CREATE TABLE accounts
(
    id        BIGINT,
    type      VARCHAR(50)    NOT NULL,
    balance   NUMERIC(19, 2) NOT NULL,
    client_id BIGINT         NOT NULL,
    CONSTRAINT pk_accounts_id PRIMARY KEY (id),
    CONSTRAINT fk_accounts_client FOREIGN KEY (client_id) REFERENCES client (id)
);

ALTER TABLE accounts
    ALTER COLUMN id SET DEFAULT nextval('ID_SEQ');
-- CREATE SEQUENCE IF NOT EXISTS ACCOUNTS_ID_SEQ START WITH 1 owned by accounts.id;
-- ALTER TABLE accounts ALTER COLUMN id SET DEFAULT nextval('ACCOUNTS_ID_SEQ');