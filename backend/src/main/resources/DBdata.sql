INSERT INTO addresses(city, state, street) VALUES ('Beograd', 'Srbija', 'Rajkova 2' );
INSERT INTO addresses(city, state, street) VALUES ('Valjevo', 'Srbija', 'Balzakova 3' );


INSERT INTO users (approved, email, enabled,firm_name,name,package_type, password, phone, pib, salt,surname,address_id) VALUES (true,'client@gmail.com',true,NULL,'Marija','STANDARD','$2a$10$cbrVImo7/Ztnix9TpeR9oOoQCFy95xff/azIrQpLahR9Rsh5/BIOS','0642715643',NULL,'h6RijmmpQ6I=','Jovic', 1);
INSERT INTO users (approved, email, enabled,firm_name,name,package_type, password, phone, pib, salt,surname,address_id) VALUES (true,'staff@gmail.com',true,NULL,'Marko',NULL,'$2a$10$cbrVImo7/Ztnix9TpeR9oOoQCFy95xff/azIrQpLahR9Rsh5/BIOS','0632136542',NULL,'h6RijmmpQ6I=','Peric', 1);
INSERT INTO users (approved, email, enabled,firm_name,name,package_type, password, phone, pib, salt,surname,address_id) VALUES (true,'admin@gmail.com',true,NULL,'Kaja',NULL,'$2a$10$cbrVImo7/Ztnix9TpeR9oOoQCFy95xff/azIrQpLahR9Rsh5/BIOS','0612456312',NULL,'h6RijmmpQ6I=','Nikic', 2);

INSERT INTO role (name) VALUES ('ROLE_ADMIN');
INSERT INTO role (name) VALUES ('ROLE_CLIENT');
INSERT INTO role (name) VALUES ('ROLE_STAFF');

INSERT INTO client(id) VALUES (1);
INSERT INTO administrator(id) VALUES (3);


INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 3);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 1);

INSERT INTO permission(description) VALUES ('clientProfile');

INSERT INTO permission_role(permission_id, role_id) VALUES (1, 2);