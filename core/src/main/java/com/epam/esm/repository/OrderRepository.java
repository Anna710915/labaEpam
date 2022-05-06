package com.epam.esm.repository;

import com.epam.esm.model.entity.Order;

import java.util.List;

public interface OrderRepository {
    long createOrder(Order order);
    List<Order> selectPaginatedUserOrders(long userId, int limit, int offset);
    Order findUserOrder(long orderId);
    int findCountAllRecords(long userId);
}
