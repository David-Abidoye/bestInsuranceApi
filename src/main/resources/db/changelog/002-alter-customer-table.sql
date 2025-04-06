--liquibase formatted sql
--changeset david:002-alter-customer-table splitStatements:false

alter table customers add column updated timestamp with time zone not null default now();