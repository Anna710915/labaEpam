USE certificates;

CREATE TABLE tag (
	tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

CREATE TABLE tags_gift_certificates(
	tag_id BIGINT,
	gift_certificate_id BIGINT
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
	orders_gift_certificates_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	order_id BIGINT,
    gift_certificate_id BIGINT,
    amount INT
);


INSERT INTO orders(order_cost, order_date, user_id)
VALUES (110, '2022-03-12 15:22:24', 1),
       (110, '2022-04-11 15:16:34', 2);

INSERT INTO orders_gift_certificates(order_id, gift_certificate_id, amount)
VALUES (1, 115, 1),
       (2, 116, 2);

INSERT INTO tags_gift_certificates (tag_id, gift_certificate_id)
VALUES (16, 116),
       (19, 116),
       (18, 116);
       
INSERT INTO users(username)
VALUES('Diana'),('Veronica');

select users.user_id,  orders_gift_certificates.*, gift_certificate.name from users
join orders on orders.user_id = users.user_id
join orders_gift_certificates ON  orders.order_id = orders_gift_certificates.order_id
join gift_certificate on orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id;

select * from orders 
join orders_gift_certificates ON  orders.order_id = orders_gift_certificates.order_id;

select * from orders_gift_certificates;
select * from orders;

select * from orders_gift_certificates;
select * from gift_certificate;
select * from orders;
SELECT * FROM tag
JOIN tags_gift_certificates ON tag.tag_id = tags_gift_certificates.tag_id
JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id;

SELECT tag.name FROM tag 
JOIN tags_gift_certificates ON tag.tag_id = tags_gift_certificates.tag_id
JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN orders ON orders.order_id = orders_gift_certificates.order_id
JOIN users ON users.user_id = orders.user_id
WHERE users.user_id = 2
GROUP BY tag.name
HAVING COUNT(*) = (SELECT MAX(tag_count) FROM (
SELECT COUNT(*) AS tag_count FROM tag
JOIN tags_gift_certificates ON tag.tag_id = tags_gift_certificates.tag_id
JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN orders ON orders.order_id = orders_gift_certificates.order_id
JOIN users ON users.user_id = orders.user_id
WHERE users.user_id = 2
GROUP BY tag.name) AS max_used_tag_query) AND SUM(price) = 
(SELECT MAX(total_cost) FROM (
SELECT SUM(price) AS total_cost FROM orders 
JOIN orders_gift_certificates ON orders.order_id = orders_gift_certificates.order_id
JOIN gift_certificate ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
WHERE orders.user_id = 2
GROUP BY tag.name) AS total_price_query);

SELECT SUM(price) FROM orders 
JOIN orders_gift_certificates ON orders.order_id = orders_gift_certificates.order_id
JOIN gift_certificate ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
WHERE orders.user_id = 2
GROUP BY tag.name;

SELECT tag.name FROM tag
JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id
JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN orders ON orders.order_id = orders_gift_certificates.order_id
JOIN (SELECT users.user_id AS user_id  FROM users
JOIN orders ON users.user_id = orders.user_id
GROUP BY users.user_id
HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost) 
FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders
JOIN users ON users.user_id = orders.user_id
GROUP BY users.user_id) AS sum_cost)) 
AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id
GROUP BY max_orders_cost_users.user_id, tag.tag_id
HAVING SUM(orders_gift_certificates.amount) = (SELECT MAX(max_tag_count) FROM (
SELECT SUM(orders_gift_certificates.amount) as max_tag_count FROM tag
JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id
JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN orders ON orders.order_id = orders_gift_certificates.order_id
JOIN (SELECT users.user_id AS user_id  FROM users
JOIN orders ON users.user_id = orders.user_id
GROUP BY users.user_id
HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost) 
FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders
JOIN users ON users.user_id = orders.user_id
GROUP BY users.user_id) AS sum_cost)) 
AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id
group by max_orders_cost_users.user_id, tag.tag_id) AS tag_counts);


SELECT SUM(orders_gift_certificates.amount) FROM tag
JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id
JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
JOIN orders ON orders.order_id = orders_gift_certificates.order_id
JOIN (SELECT users.user_id AS user_id  FROM users
JOIN orders ON users.user_id = orders.user_id
GROUP BY users.user_id
HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost) 
FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders
JOIN users ON users.user_id = orders.user_id
GROUP BY users.user_id) AS sum_cost)) 
AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id
group by max_orders_cost_users.user_id, tag.tag_id;


SELECT gift_certificate_id, name, description, price, duration, create_date, last_update_date  FROM (
SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id 
JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
WHERE tag.name = "#beautiful"
UNION SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id 
JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
WHERE tag.name = "#like"
UNION SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id 
JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
WHERE tag.name = "#mascara") AS used_tags_certificates
GROUP BY gift_certificate_id
HAVING COUNT(tag_name) = 3;

