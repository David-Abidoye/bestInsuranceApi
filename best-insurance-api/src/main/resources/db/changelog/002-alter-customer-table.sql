--liquibase formatted sql
--changeset david:002-alter-customer-table splitStatements:false

alter table customers add column updated timestamp with time zone not null default now();
alter table customers add column telephone_number varchar(20);
alter table customers add column birth_date date not null;