-- liquibase formatted sql

-- changeset novak:create_table_notification_task

    CREATE TABLE notification_task (
        id serial primary key,
        chat_id int not null,
        notification varchar,
        date_time timestamp not null
    )