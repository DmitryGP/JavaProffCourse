insert into address(street)
values ('Ordynka'), ('Petrovka');

insert into client(id, name, address_id)
values(nextval('client_SEQ'), 'Aleksey', 1), (nextval('client_SEQ'), 'Evlampiy', 2);

insert into phone(number, client_id)
values('123-45-67', 1), ('675-43-21', 1),
('987-76-54', 2), ('456-78-90', 2);