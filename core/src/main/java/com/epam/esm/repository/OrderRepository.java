package com.epam.esm.repository;

import com.epam.esm.model.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Order repository.
 * @author Anna Merkul
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrdersByUser_UserId(long userId, Pageable pageable);

    /**
     * Find user order.
     *
     * @param orderId the order id
     * @return the order
     */

    Order findOrderByOrderId(long orderId);

    /**
     * Find count all records int.
     *
     * @param userId the user id
     * @return the int
     */
    @Query(value = "SELECT COUNT(*) FROM orders WHERE user_id = :id", nativeQuery = true)
    int findCountAllRecords(@Param("id") long userId);
}
