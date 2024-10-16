INSERT INTO tb_user (name, email, password) VALUES ('Nina Brown', 'nina@gmail.com', '$2a$10$t3NzJ9klO7ZoAvC4wYrcru3HD1wb6b1LpluxL4XBd97hpShC4vxsq');
INSERT INTO tb_user (name, email, password) VALUES ('Leia Red', 'leia@gmail.com', '$2a$10$t3NzJ9klO7ZoAvC4wYrcru3HD1wb6b1LpluxL4XBd97hpShC4vxsq');
INSERT INTO tb_user (name, email, password) VALUES ('Lucas Jorge', 'lucas@gmail.com', '$2a$10$t3NzJ9klO7ZoAvC4wYrcru3HD1wb6b1LpluxL4XBd97hpShC4vxsq');

INSERT INTO tb_role (name) VALUES ('ROLE_OPERATOR');
INSERT INTO tb_role (name) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 2);
