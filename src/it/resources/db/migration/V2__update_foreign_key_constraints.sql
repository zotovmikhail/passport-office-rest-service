alter table passports
    drop constraint passports_owner_id_fkey,
    add constraint passports_owner_id_fkey foreign key (owner_id) references persons (id)
        on delete no action on update no action;
