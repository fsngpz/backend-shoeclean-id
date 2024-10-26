CREATE TYPE service_type AS enum ('DEEP_CLEANING', 'REPAIR', 'RAPID_CLEANING');
CREATE TYPE order_status AS enum ('PENDING', 'ON_PICKUP', 'ON_DELIVERY', 'CLEANING', 'DELIVERED', 'CANCELED');
CREATE TYPE voucher_type AS enum ('DISCOUNT', 'FREE_PAIR');
CREATE TYPE amount_type AS enum ('PERCENTAGE', 'AMOUNT');
CREATE TYPE transaction_status AS enum ('PAID', 'UNPAID', 'CANCELED', 'SUCCESS');
CREATE TYPE transaction_method AS enum ('CASH_ON_DELIVERY', 'BANK_TRANSFER');
CREATE TYPE notification_channel AS enum ('EMAIL', 'PUSH', 'SMS');
CREATE TYPE notification_status AS enum ('PENDING', 'SUCCESS', 'FAILED');

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
CREATE UNIQUE INDEX users_mobile_uindex ON users (mobile);

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

/*
 * Services
 */
CREATE TABLE services
(
    id                  BIGSERIAL
        CONSTRAINT services_id_pk
            PRIMARY KEY,
    type                service_type                                       NOT NULL,
    description         text                                               NOT NULL,
    city                text                                               NOT NULL,
    price               text                                               NOT NULL,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);
CREATE UNIQUE INDEX services_type_uindex ON users (type);

/*
 * Vouchers
 */
CREATE TABLE vouchers
(
    id                  BIGSERIAL
        CONSTRAINT vouchers_id_pk
            PRIMARY KEY,
    code                text                                               NOT NULL,
    quantity            integer                                            NOT NULL,
    type                voucher_type                                       NOT NULL,
    amount_type         amount_type                                        NOT NULL,
    amount              text                                               NOT NULL,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    expired_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);
CREATE UNIQUE INDEX vouchers_code_uindex ON users (code);

/*
 * Orders
 */
CREATE TABLE orders
(
    id                  BIGSERIAL
        CONSTRAINT orders_id_pk
            PRIMARY KEY,
    account_id             bigint
        CONSTRAINT orders_account_id_fk
            REFERENCES accounts
            ON UPDATE CASCADE ON DELETE CASCADE,
    address_id             bigint
        CONSTRAINT orders_address_id_fk
            REFERENCES addresses
            ON UPDATE CASCADE ON DELETE CASCADE,
    service_id             bigint
        CONSTRAINT orders_service_id_fk
            REFERENCES services
            ON UPDATE CASCADE ON DELETE CASCADE,
    voucher_id             bigint
        CONSTRAINT orders_voucher_id_fk
            REFERENCES vouchers
            ON UPDATE CASCADE ON DELETE CASCADE,
    status              order_status                                       NOT NULL,
    total_pairs         text                                               NOT NULL,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);

/*
 * Transactions
 */
CREATE TABLE transactions
(
    id                  BIGSERIAL
        CONSTRAINT transactions_id_pk
            PRIMARY KEY,
    order_id             bigint
        CONSTRAINT transactions_order_id_fk
            REFERENCES orders
            ON UPDATE CASCADE ON DELETE CASCADE,
    total_amount        text                                               NOT NULL,
    deduction           text                                               NOT NULL,
    status              transaction_status                                 NOT NULL,
    method              transaction_method                                 NOT NULL,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);

/*
 * Orders Sneakers
 */
CREATE TABLE orders_sneakers
(
    id                  BIGSERIAL
        CONSTRAINT orders_sneakers_id_pk
            PRIMARY KEY,
    order_id             bigint
        CONSTRAINT orders_sneakers_order_id_fk
            REFERENCES orders
            ON UPDATE CASCADE ON DELETE CASCADE,
    sneaker_id           bigint
        CONSTRAINT orders_sneakers_sneaker_id_fk
            REFERENCES sneakers
            ON UPDATE CASCADE ON DELETE CASCADE,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);
CREATE UNIQUE INDEX orders_sneakers_order_id_sneaker_id_uindex ON orders_sneakers (order_id, sneaker_id);

/*
 * Notifications
 */
CREATE TABLE notifications
(
    id                  BIGSERIAL
        CONSTRAINT notifications_id_pk
            PRIMARY KEY,
    account_id             bigint
        CONSTRAINT notifications_account_id_fk
            REFERENCES accounts
            ON UPDATE CASCADE ON DELETE CASCADE,
    channel             notification_channel                               NOT NULL,
    type                text                                               NOT NULL,
    content             text                                               NOT NULL,
    status              notification_status                                NOT NULL,
    created_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    creator_id          text                                               NOT NULL,
    updated_at          timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater_id          text                                               NOT NULL,
    version             bigint                   DEFAULT 0                 NOT NULL
);
