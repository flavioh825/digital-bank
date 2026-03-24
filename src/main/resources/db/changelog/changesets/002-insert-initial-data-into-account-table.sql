--liquibase formatted sql

--changeset flavio-henrique:2
INSERT INTO account (owner_name, balance, version) VALUES ('Maria', 1000.00, 0);
INSERT INTO account (owner_name, balance, version) VALUES ('Roberto', 500.00, 0);

--rollback DELETE FROM account WHERE owner_name IN ('Maria', 'Roberto');