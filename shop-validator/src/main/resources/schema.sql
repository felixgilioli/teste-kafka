create table product (
    id bigint generated by default as identity,
    identifier varchar(100) not null,
    amount int not null,
    primary key (id)
);