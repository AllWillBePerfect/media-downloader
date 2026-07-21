create table download_jobs
(
    id         uuid primary key,
    user_id    bigint      not null,
    url        text        not null,
    type       varchar(20) not null,
--  delivery_type varchar(20) not null,
    status     varchar(20) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table delivery_targets
(
    id            uuid primary key,
    job_id        uuid        not null unique,
    delivery_type varchar(20) not null,
    target        text        not null,
    created_at    timestamptz not null,

    constraint fk_delivery_target_job
        foreign key (job_id)
            references download_jobs (id)
            on delete cascade
);

create table downloaded_files
(
    id         uuid primary key,
    job_id     uuid        not null unique,
    path       text        not null,
    file_name  text        not null,
    size       bigint      not null,
    mime_type  text        not null,
    expires_at timestamptz null,
    created_at timestamptz not null,

    constraint fk_downloaded_file_job
        foreign key (job_id)
            references download_jobs (id)
            on delete cascade
);

create table storage_usage
(
    id             integer primary key,
    limit_bytes    bigint not null,
    used_bytes     bigint not null default 0,
    reserved_bytes bigint not null default 0,

    constraint storage_usage_singleton check (id = 1)
);

insert into storage_usage (id, limit_bytes, used_bytes, reserved_bytes)
values (1, 10737418240, 0, 0);

create table storage_reservations
(
    job_id         uuid primary key,
    reserved_bytes bigint not null,

    constraint fk_storage_reservation_job
        foreign key (job_id)
            references download_jobs (id)
            on delete cascade
);