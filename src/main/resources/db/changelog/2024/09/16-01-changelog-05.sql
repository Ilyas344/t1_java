-- liquibase formatted sql
CREATE TABLE IF NOT EXISTS accounts
(
    id           BIGSERIAL PRIMARY KEY,
    account_type VARCHAR(255)   NOT NULL,
    balance      DECIMAL(19, 2) NOT NULL,
    is_blocked   BOOLEAN        NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS transactions
(
    id         BIGSERIAL PRIMARY KEY,
    amount     DECIMAL(19, 2) NOT NULL,
    client_id  BIGINT         NOT NULL,
    account_id BIGINT         NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);
