package com.epam.esm.service;

import com.epam.esm.model.dto.OrderDto;

import java.util.List;

public interface OrderService {
    long createOrder(OrderDto orderDto);
    List<OrderDto> findPaginatedUserOrders(long userId, int size, int offset);
    OrderDto findUserOrder(long orderId);
    int findTotalRecords(long userId);
}
