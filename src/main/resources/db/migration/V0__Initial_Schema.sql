
/*
 * Users
 */
CREATE TABLE users
(
    id                  BIGSERIAL
        CONSTRAINT users_id_pk
            PRIMARY KEY,
    email               text                                               NOT NULL,
    password            text                                               NOT NULL,
    username            text,
    mobile              text,
    attributes          text,
    token_uid           text,
    email_verified_at   timestamp WITH TIME ZONE,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);

CREATE UNIQUE INDEX users_email_uindex ON users (LOWER(email));
CREATE UNIQUE INDEX users_mobile_uindex ON users (email);

/*
 * Roles
 */
CREATE TABLE roles
(
    id                  BIGSERIAL
        CONSTRAINT roles_id_pk
            PRIMARY KEY,
    name                text                                               NOT NULL,
    description         text,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);

CREATE UNIQUE INDEX roles_name_uindex ON roles(UPPER(name));

/*
 * Users Roles
 */
CREATE TABLE users_roles
(
    id                  BIGSERIAL
        CONSTRAINT users_roles_id_pk
            PRIMARY KEY,
    user_id             bigint
        CONSTRAINT users_roles_user_id_fk
            REFERENCES users
            ON UPDATE CASCADE ON DELETE CASCADE,
    role_id             bigint
        CONSTRAINT users_roles_role_id_fk
            REFERENCES roles
            ON UPDATE CASCADE ON DELETE CASCADE,
    version             bigint                   DEFAULT 0                 NOT NULL
);

/*
 * Accounts
 */
CREATE TABLE accounts
(
    id                  BIGSERIAL
        CONSTRAINT accounts_id_pk
            PRIMARY KEY,
    user_id             bigint
        CONSTRAINT accounts_user_id_fk
            REFERENCES users
            ON UPDATE CASCADE ON DELETE CASCADE,
    name                text                                               NOT NULL,
    profile_picture_url text,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);

CREATE UNIQUE INDEX accounts_user_id_uindex ON accounts (user_id);

/*
 * Sneakers
 */
CREATE TABLE sneakers
(
    id                  BIGSERIAL
        CONSTRAINT sneakers_id_pk
            PRIMARY KEY,
    account_id             bigint
        CONSTRAINT sneakers_account_id_fk
            REFERENCES accounts
            ON UPDATE CASCADE ON DELETE CASCADE,
    brand               text                                               NOT NULL,
    color               text                                               NOT NULL,
    image_url           text,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);

/*
 * Sneakers
 */
CREATE TABLE addresses
(
    id                  BIGSERIAL
        CONSTRAINT addresses_id_pk
            PRIMARY KEY,
    account_id             bigint
        CONSTRAINT addresses_account_id_fk
            REFERENCES accounts
            ON UPDATE CASCADE ON DELETE CASCADE,
    label               text,
    line                text                                               NOT NULL,
    city                text                                               NOT NULL,
    district            text                                               NOT NULL,
    subdistrict         text                                               NOT NULL,
    state               text                                               NOT NULL,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);
