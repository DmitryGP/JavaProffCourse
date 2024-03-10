insert into client(name)
values('Aleksey'), ('Evlampiy');

insert into address(street, client_id)
values ('Ordynka', 1), ('Petrovka', 2);

insert into phone(number, client_id)
values('123-45-67', 1), ('675-43-21', 1),
('987-76-54', 2), ('456-78-90', 2);