
create sequence client_SEQ start with 1 increment by 1;

create table if not exists address
(
    id   bigserial,
    street varchar(50),
    primary key (id)
);

create table client
(
    id   bigint not null primary key,
    name varchar(50),
    address_id bigint references address (id) on delete cascade
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(50),
    client_id bigint references client (id)
);
