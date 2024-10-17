insert into users (username, email, password)
values ('John Doe', 'johndoe@gmail.com', '$2a$10$Xl0yhvzLIaJCDdKBS0Lld.ksK7c2Zytg/ZKFdtIYYQUv8rUfvCR4W'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$fFLij9aYgaNCFPTL9WcA/uoCRukxnwf.vOQ8nrEEOskrCNmGsxY7m');

insert into users_roles (user_id, role)
values (a1b2c3d4-e5f6-7890-1234- 567890abcdef, 'ROLE_ADMIN'),
       (b2c3d4e5-f678-9012-3456- 7890abcdef01, 'ROLE_USER'),
       (c3d4e5f6-7890-1234-5678- 90abcdef0123, 'ROLE_USER');