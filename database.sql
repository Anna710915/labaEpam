USE certificates;

CREATE TABLE gift_certificate(
	gift_certificate_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(120),
    price DECIMAL(8,2),
    duration INT,
    create_date TIMESTAMP,
    last_update_date TIMESTAMP
);

CREATE TABLE tag (
	tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100)
);

ALTER TABLE tag
MODIFY COLUMN name VARCHAR(100) UNIQUE;

CREATE TABLE tags_gift_certificates(
	tag_id BIGINT,
	gift_certificate_id BIGINT
);

ALTER TABLE tags_gift_certificates
ADD CONSTRAINT certificate_key
FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate(gift_certificate_id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE tags_gift_certificates
ADD CONSTRAINT tag_key
FOREIGN KEY (tag_id) REFERENCES tag(tag_id)
ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE users(
	user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
	username VARCHAR(50));

CREATE TABLE orders(
	order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_cost DECIMAL(8,2),
    order_date TIMESTAMP,
    user_id BIGINT
); 

ALTER TABLE orders
ADD CONSTRAINT user_key
FOREIGN KEY (user_id) REFERENCES users(user_id)
ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE orders_gift_certificates(
	orders_gift_certificates_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	order_id BIGINT,
    gift_certificate_id BIGINT,
    amount INT
);

ALTER TABLE orders_gift_certificates
ADD CONSTRAINT order_key
FOREIGN KEY (order_id) REFERENCES orders(order_id)
ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE orders_gift_certificates
ADD CONSTRAINT orders_certificate_key
FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate(gift_certificate_id)
ON DELETE CASCADE ON UPDATE CASCADE;


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
HAVING COUNT(tag_name) = 777;

-- -----------------------------------

CALL `insert_tags`();

select * from tag;

CALL `insert_users`();

select * from users;

CALL `insert_certificates`();

select * from gift_certificate;

CALL `insert_certificate_tags`();

select * from tags_gift_certificates;

CALL `insert_orders_gift_certificates`();

select * from orders;

select * from orders_gift_certificates;







