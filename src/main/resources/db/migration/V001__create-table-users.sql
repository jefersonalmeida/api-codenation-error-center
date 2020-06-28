create table if not exists public.users
(
    id         uuid         not null,
    name       varchar(100) not null,
    email      varchar(100) not null,
    password   varchar(100) not null,
    role       varchar(255),
    created_at timestamp    not null,
    updated_at timestamp    not null,
    constraint users_pkey
        primary key (id),
    constraint users_unique
        unique (id, email)
);

create unique index if not exists users_unique
    on public.users (id, email);

create index if not exists users_index
    on public.users (id, email);
