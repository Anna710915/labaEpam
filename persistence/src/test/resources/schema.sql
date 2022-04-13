CREATE SCHEMA certificates;

create table tag(
     tag_id bigint primary key auto_increment,
     name varchar(100)
);

create table gift_certificate(
    gift_certificate_id bigint primary key auto_increment,
    name varchar(100),
    description varchar(120),
    price decimal(8,2),
    duration int,
    create_date timestamp,
    last_update_date timestamp
);

create table tags_gift_certificates(
    tag_id bigint,
    gift_certificate_id bigint,
    constraint tag_id_foreign_key
    foreign key (tag_id) references tag(tag_id)
    on delete cascade on update cascade,
    constraint gift_certificate_id_foreign_key
    foreign key (gift_certificate_id) references gift_certificate(gift_certificate_id)
    on delete cascade on update cascade
);
