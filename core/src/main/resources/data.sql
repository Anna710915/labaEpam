INSERT INTO tag(name)
VALUES
('#like'),('#ring'),('#cream'),('#mascara');


INSERT INTO gift_certificate(create_date, description, duration, last_update_date, name, price)
VALUES ('2022-01-01 15:40:01', 'nkdcdnc', 35, '2022-03-01 14:37:33', 'ZIKO', 150),
       ('2022-02-01 17:40:01','dskckjn', 30, '2022-04-01 10:37:33', 'Mila', 50);

INSERT INTO tags_gift_certificates (tag_id, gift_certificate_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 2);

INSERT INTO users(username)
VALUES('Anna'),('Daniil');

INSERT INTO orders(order_cost, order_date, user_id)
VALUES (150, '2022-03-12 15:22:24', 1),
       (150, '2022-04-11 15:16:34', 2);

INSERT INTO orders_gift_certificates(order_id, gift_certificate_id, amount)
VALUES (1, 1, 1),
       (2, 2, 3);