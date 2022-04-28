CREATE SCHEMA certificates;

create table tag(
     tag_id bigint primary key auto_increment,
     name varchar(100)
);

create table gift_certificate(
    gift_certificate_id bigint primary key auto_increment,
    create_date timestamp,
    description varchar(120),
    duration int,
    last_update_date timestamp,
    name varchar(100),
    price decimal(8,2)
);

create table tags_gift_certificates(
    tag_id bigint,
    gift_certificate_id bigint
);

CREATE TABLE users(
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50));

CREATE TABLE orders(
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_cost DECIMAL(8,2),
    order_date TIMESTAMP,
    user_id BIGINT
);

CREATE TABLE orders_gift_certificates(
    orders_gift_certificates_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    gift_certificate_id BIGINT,
    amount INT
);
