package com.epam.esm.service;

import com.epam.esm.model.dto.OrderDto;

import java.util.List;

/**
 * The interface Order service.
 * @author Anna Merkul
 */
public interface OrderService {
    /**
     * Create order long.
     *
     * @param orderDto the order dto
     * @return the long
     */
    long createOrder(OrderDto orderDto);

    List<OrderDto> findPaginatedUserOrders(long userId, int page, int size);

    /**
     * Find user order order dto.
     *
     * @param orderId the order id
     * @return the order dto
     */
    OrderDto findUserOrder(long orderId);

    /**
     * Find total records int.
     *
     * @param userId the user id
     * @return the int
     */
    int findTotalRecords(long userId);
}
