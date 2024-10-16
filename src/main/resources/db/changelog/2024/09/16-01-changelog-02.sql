-- liquibase formatted sql

-- changeset e_cha:1726477659739-1
ALTER TABLE clients
    ADD first_name VARCHAR(255);
ALTER TABLE clients
    ADD last_name VARCHAR(255);
ALTER TABLE clients
    ADD middle_name VARCHAR(255);


