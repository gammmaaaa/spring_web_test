CREATE TABLE accounts
(
    id BIGINT,
    type VARCHAR(50) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL,
    client_id BIGINT NOT NULL,
    CONSTRAINT pk_accounts_id PRIMARY KEY (id),
    CONSTRAINT fk_accounts_client FOREIGN KEY (client_id) REFERENCES client(id)
);