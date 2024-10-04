-- liquibase formatted sql

-- changeset e_cha:
CREATE SEQUENCE IF NOT EXISTS clients_seq START WITH 1 INCREMENT BY 50;

-- changeset e_cha:1726476397331-2
CREATE TABLE clients
(
    id BIGINT NOT NULL,
    CONSTRAINT pk_client PRIMARY KEY (id)
);

