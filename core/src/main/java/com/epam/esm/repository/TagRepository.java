package com.epam.esm.repository;

import com.epam.esm.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository contains methods for tags.
 *
 * @author Anna Merkul
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findTagByName(String name);

    Tag findTagById(long id);

    /**
     * Delete a tag.
     *
     * @param id the id
     * @return the boolean
     */
    int deleteTagById(long id);

    /**
     * Find widely user tag with highest orders cost list.
     *
     * @return the list
     */
    @Query(value = "SELECT tag.* FROM tag\n" +
            "JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id\n" +
            "JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id\n" +
            "JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id\n" +
            "JOIN orders ON orders.order_id = orders_gift_certificates.order_id\n" +
            "JOIN (SELECT users.user_id AS user_id  FROM users\n" +
            "JOIN orders ON users.user_id = orders.user_id\n" +
            "GROUP BY users.user_id\n" +
            "HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost) \n" +
            "FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders\n" +
            "JOIN users ON users.user_id = orders.user_id\n" +
            "GROUP BY users.user_id) AS sum_cost)) \n" +
            "AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id\n" +
            "GROUP BY max_orders_cost_users.user_id, tag.tag_id\n" +
            "HAVING SUM(orders_gift_certificates.amount) = (SELECT MAX(max_tag_count) FROM (\n" +
            "SELECT SUM(orders_gift_certificates.amount) as max_tag_count FROM tag\n" +
            "JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id\n" +
            "JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id\n" +
            "JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id\n" +
            "JOIN orders ON orders.order_id = orders_gift_certificates.order_id\n" +
            "JOIN (SELECT users.user_id AS user_id  FROM users\n" +
            "JOIN orders ON users.user_id = orders.user_id\n" +
            "GROUP BY users.user_id\n" +
            "HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost) \n" +
            "FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders\n" +
            "JOIN users ON users.user_id = orders.user_id\n" +
            "GROUP BY users.user_id) AS sum_cost)) \n" +
            "AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id\n" +
            "group by max_orders_cost_users.user_id, tag.tag_id) AS tag_counts)", nativeQuery = true)
    List<Tag> findWidelyUserTagWithHighestOrdersCost();

    /**
     * Count all tags int.
     *
     * @return the int
     */
    @Query(value = "SELECT COUNT(*) FROM tag", nativeQuery = true)
    int countAllTags();
}
