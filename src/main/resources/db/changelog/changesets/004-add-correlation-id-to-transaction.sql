--liquibase formatted sql

--changeset flavio-henrique:4
ALTER TABLE transaction ADD COLUMN correlation_id VARCHAR(255);
ALTER TABLE transaction ADD CONSTRAINT uk_transaction_correlation_id UNIQUE (correlation_id);

--rollback ALTER TABLE transaction DROP CONSTRAINT uk_transaction_correlation_id;
--rollback ALTER TABLE transaction DROP COLUMN correlation_id;
