create table if not exists public.events
(
    id               uuid         not null,
    level            varchar(20)  not null,
    description      varchar(100) not null,
    log              text         not null,
    origin           varchar(100) not null,
    date             timestamp    not null,
    quantity         integer      not null,
    created_at       timestamp,
    created_by       varchar(255),
    last_modified_at timestamp,
    last_modified_by varchar(255),
    constraint events_pkey
        primary key (id)
);

create unique index if not exists events_unique
    on public.events (id);

create index if not exists users_index
    on public.events (id, level, origin);
