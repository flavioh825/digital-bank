--liquibase formatted sql

--changeset flavio-henrique:5
INSERT INTO account (owner_name, balance, version) VALUES ('Ana', 1500.00, 0);
INSERT INTO account (owner_name, balance, version) VALUES ('Fernando', 200.00, 0);
INSERT INTO account (owner_name, balance, version) VALUES ('Paula', 3000.00, 0);

--rollback DELETE FROM account WHERE owner_name IN ('Ana', 'Fernando', 'Paula');
