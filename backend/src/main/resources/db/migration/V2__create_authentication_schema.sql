CREATE TABLE users (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_username UNIQUE (username)
);

CREATE TYPE roles_enum AS ENUM (
    'ROLE_USER',
    'ROLE_MODERATOR',
    'ROLE_ADMIN',
    'ROLE_SUPERADMIN'
);

create table user_roles (
    id INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    user_id INT NOT NULL,
    role roles_enum NOT NULL,

    CONSTRAINT u_user_role UNIQUE (user_id, role),
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);
