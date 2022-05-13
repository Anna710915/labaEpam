package com.epam.esm.repository;

import com.epam.esm.model.entity.Order;

import java.util.List;

/**
 * The interface Order repository.
 * @author Anna Merkul
 */
public interface OrderRepository {
    /**
     * Create order long.
     *
     * @param order the order
     * @return the long
     */
    long createOrder(Order order);

    /**
     * Select paginated user orders list.
     *
     * @param userId the user id
     * @param limit  the limit
     * @param offset the offset
     * @return the list
     */
    List<Order> selectPaginatedUserOrders(long userId, int limit, int offset);

    /**
     * Find user order.
     *
     * @param orderId the order id
     * @return the order
     */
    Order findUserOrder(long orderId);

    /**
     * Find count all records int.
     *
     * @param userId the user id
     * @return the int
     */
    int findCountAllRecords(long userId);
}
