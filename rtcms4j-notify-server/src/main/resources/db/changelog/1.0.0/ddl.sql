--liquibase formatted sql

--changeset Enzhine:1.0.0:1
create extension if not exists "uuid-ossp";

create table configuration_sync_state(
    ---- tech fields
    id bigserial primary key not null,
    created_at timestamptz not null default now(),
    ---- entity fields
    configuration_id bigint not null references configuration (id) on delete cascade,
    source_identity varchar(64) not null,
    commit_hash varchar(64),
    is_online boolean
);
--rollback drop table if exists configuration_sync_state;

create index ix_configuration_sync_state__configuration_id_created_at on configuration_sync_state (configuration_id, created_at);
--rollback drop index if exists ix_configuration_sync_state__configuration_id_created_at;
