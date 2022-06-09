package com.epam.esm.repository.query;

public final class TagQuery {

    public static final String FIND_WIDELY_USER_TAG_WITH_HIGHEST_ORDERS_COST = """
            SELECT tag.* FROM tag
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
            group by max_orders_cost_users.user_id, tag.tag_id) AS tag_counts)""";

    public static final String COUNT_ALL_TAGS = """
            SELECT COUNT(*) FROM tag""";
}
