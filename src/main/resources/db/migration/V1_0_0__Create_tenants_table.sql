create table tenants
(
    id         uuid        not null primary key,
    slug       varchar(8) unique,
    name       varchar(64) not null,
    created_at timestamp(6),
    created_by uuid,
    updated_at timestamp(6),
    updated_by uuid,
    deleted_at timestamp(6),
    deleted_by uuid
);

comment on column tenants.slug is 'Human readable tenant id';
comment on column tenants.name is 'Tenant name';
