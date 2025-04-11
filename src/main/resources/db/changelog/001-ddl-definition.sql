--liquibase formatted sql
--changeset david:001-ddl-definition splitStatements:false

create
extension if not exists "uuid-ossp";

create table countries
(
    country_id uuid primary key default uuid_generate_v4(),
    name       varchar(64) not null,
    population int
);

create table states
(
    state_id   uuid primary key default uuid_generate_v4(),
    name       varchar(64)                            not null,
    country_id uuid references countries (country_id) not null,
    population int
);

create table cities
(
    city_id    uuid primary key default uuid_generate_v4(),
    country_id uuid references countries (country_id) not null,
    state_id   uuid references states (state_id),
    name       varchar(64)                            not null,
    population int
);

create table addresses
(
    address_id  uuid primary key default uuid_generate_v4(),
    country_id  uuid references countries (country_id) not null,
    city_id     uuid references cities (city_id)       not null,
    state_id    uuid references states (state_id),
    address     varchar(128)                           not null,
    postal_code varchar(16)
);

create table customers
(
    customer_id uuid primary key                  default uuid_generate_v4(),
    name     varchar(64)              not null,
    surname  varchar(64)              not null,
    email    varchar(320)             not null,
    created  timestamp with time zone not null default now(),
    address  uuid references addresses (address_id)
);

create table policies
(
    policy_id   uuid primary key default uuid_generate_v4(),
    name        varchar(16)              not null,
    description text,
    price       numeric(6, 2)            not null,
    created     timestamp with time zone not null,
    updated     timestamp with time zone not null
);

create table subscriptions
(
    policy_id   uuid references policies (policy_id)    not null,
    customer_id uuid references customers (customer_id) not null,
    primary key (policy_id, customer_id),
    start_date  date                                    not null,
    end_date    date                                    not null,
    paid_price  numeric(6, 2)                           not null,
    created     timestamp with time zone                not null,
    updated     timestamp with time zone                not null
);

create table coverages
(
    coverage_id uuid primary key default uuid_generate_v4(),
    name        varchar(16) not null,
    description text
);

create table policy_coverages
(
    policy_id   uuid references policies (policy_id)    not null,
    coverage_id uuid references coverages (coverage_id) not null,
    primary key (policy_id, coverage_id)
);