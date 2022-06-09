package com.epam.esm.service.impl;

import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.MessageLanguageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = OrderService.class)
class OrderServiceImplTest {

    private OrderService orderService;
    @Autowired
    private MessageLanguageUtil messageLanguageUtil;
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @BeforeEach
    void setUp(){
        orderService = new OrderServiceImpl(orderRepository, messageLanguageUtil);
    }

    @Test
    void findPaginatedUserOrders() {
        Order order = new Order();
        order.setUser(new User());
        order.setOrderCertificates(new ArrayList<>());
        Mockito.when(orderRepository.findOrdersByUser_UserId(1L, PageRequest.of(0, 1))).thenReturn(List.of(order));
        orderService.findPaginatedUserOrders(1L, 1, 1);
    }

    @Test
    void findUserOrder() {
        Order order = new Order();
        order.setUser(new User());
        order.setOrderCertificates(new ArrayList<>());
        Mockito.when(orderRepository.findOrderByOrderId(1L)).thenReturn(order);
        orderService.findUserOrder(1L);
    }

    @AfterEach
    void destroy(){
        orderService = null;
    }
}