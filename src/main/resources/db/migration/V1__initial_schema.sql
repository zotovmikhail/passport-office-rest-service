create table persons
(
    id       varchar primary key not null,
    name     varchar             not null,
    birthday date                not null,
    country  varchar             not null
);

create table passport_states
(
    state varchar primary key not null
);

insert into passport_states (state)
values ('ACTIVE'),
       ('LOST');

create table passports
(
    number          varchar primary key not null,
    given_date      date                not null,
    department_code varchar             not null,
    state           varchar             not null,
    owner_id        varchar             not null,
    foreign key (state) references passport_states (state),
    foreign key (owner_id) references persons (id)
);