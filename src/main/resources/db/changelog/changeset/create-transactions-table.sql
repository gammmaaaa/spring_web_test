CREATE TABLE transactions
(
    id               BIGINT,
    amount           NUMERIC(19, 2) NOT NULL,
    time_transaction TIMESTAMP      NOT NULL,
    account_id       BIGINT         NOT NULL,
    CONSTRAINT pk_transactions_id PRIMARY KEY (id),
    CONSTRAINT fk_transactions_accounts FOREIGN KEY (account_id) REFERENCES accounts (id)
);

ALTER TABLE transactions
    ALTER COLUMN id SET DEFAULT nextval('ID_SEQ');
-- CREATE SEQUENCE IF NOT EXISTS TRANSACTIONS_ID_SEQ START WITH 1 owned by transactions.id;
-- ALTER TABLE transactions ALTER COLUMN id SET DEFAULT nextval('TRANSACTIONS_ID_SEQ');