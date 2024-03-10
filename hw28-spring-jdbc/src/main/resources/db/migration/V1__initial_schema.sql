
create table if not exists client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table if not exists address
(
    id   bigserial not null primary key,
    street varchar(50),
    client_id bigint references client (id)
);

create table if not exists phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint references client (id)
);
