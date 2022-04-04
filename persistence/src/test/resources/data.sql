INSERT INTO tag(name)
VALUES
('#like'),('#ring'),('#cream'),('#mascara');

INSERT INTO gift_certificate(name, description, price, duration,
                             create_date, last_update_date)
VALUES ('ZIKO','nkdcdnc', 150, 35, '2022-01-01 15:40:01', '2022-03-01 14:37:33'),
       ('Mila','dskckjn', 50, 30, '2022-02-01 17:40:01', '2022-04-01 10:37:33');

INSERT INTO tags_gift_certificates(tag_id, gift_certificate_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 2);