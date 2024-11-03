CREATE TABLE transactions
(
    id BIGINT,
    amount NUMERIC(19,2) NOT NULL,
    time_transaction TIMESTAMP NOT NULL,
    account_id BIGINT NOT NULL,
    CONSTRAINT pk_transactions_id PRIMARY KEY (id),
    CONSTRAINT fk_transactions_accounts FOREIGN KEY (account_id) REFERENCES accounts(id)
);