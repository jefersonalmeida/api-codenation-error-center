create table users
(
    id       UUID         NOT NULL,
    name     varchar(255) NOT NULL,
    email    varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    role     varchar(255),
    created  timestamp    NOT NULL,
    updated  timestamp    NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_id_email_unique UNIQUE (id, email)
);

create table events
(
    id          UUID         NOT NULL,
    user_id     UUID,
    level       varchar(255) NOT NULL,
    description varchar(255) NOT NULL,
    log         varchar(255) NOT NULL,
    origin      varchar(255) NOT NULL,
    date        timestamp    NOT NULL,
    quantity    int4         NOT NULL,
    created     timestamp    NOT NULL,
    updated     timestamp    NOT NULL,
    CONSTRAINT events_pkey PRIMARY KEY (id),
    CONSTRAINT events_user_id_foreign FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

