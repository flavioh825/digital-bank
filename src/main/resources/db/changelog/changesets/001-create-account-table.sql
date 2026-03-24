--liquibase formatted sql

--changeset flavio-henrique:1
CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    owner_name VARCHAR(100) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    version BIGINT NOT NULL
);

--rollback DROP TABLE account;