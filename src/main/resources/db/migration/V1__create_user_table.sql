CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password_hash VARCHAR(1024) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT pg_catalog.now(),
    updated_at TIMESTAMP NOT NULL DEFAULT pg_catalog.now()
);

CREATE UNIQUE INDEX ux_users_email ON users (lower(email));