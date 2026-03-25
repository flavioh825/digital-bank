--liquibase formatted sql

--changeset flavio-henrique:3
CREATE TABLE transaction (
     id BIGSERIAL PRIMARY KEY,
     account_id BIGINT NOT NULL,
     amount DECIMAL(19, 2) NOT NULL,
     created_at TIMESTAMP NOT NULL,
     description VARCHAR(255),
     CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account(id)
);
CREATE INDEX idx_transaction_account_date ON transaction(account_id, created_at DESC);

--rollback DROP INDEX IF EXISTS idx_transaction_account_date;
--rollback DROP TABLE IF EXISTS transaction;