CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_username UNIQUE (username)
);

CREATE TABLE roles (
    id SERIAL NOT NULL,
    name varchar(60) NOT NULL,
    PRIMARY KEY (id),

    CONSTRAINT uk_roles_name UNIQUE (name)
);

create table user_roles (
    user_role_id SERIAL PRIMARY KEY,
    username VARCHAR(40) NOT NULL,
    role VARCHAR(40) NOT NULL,

    CONSTRAINT u_username_role UNIQUE (username, role),
    CONSTRAINT fk_user_username FOREIGN KEY (username) REFERENCES users (username)
);
