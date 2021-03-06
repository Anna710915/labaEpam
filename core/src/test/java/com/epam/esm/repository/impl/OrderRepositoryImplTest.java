package com.epam.esm.repository.impl;

import com.epam.esm.config.DevelopmentConfig;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderCertificate;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.OrderRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DevelopmentConfig.class)
@ActiveProfiles("dev")
public class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void createOrder(){
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setOrderCost(BigDecimal.valueOf(150));
        order.setUser(new User(1L, "Anna"));
        OrderCertificate orderCertificate = new OrderCertificate();
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setPrice(BigDecimal.valueOf(150));
        giftCertificate.setDuration(35);
        giftCertificate.setName("ZIKO");
        giftCertificate.setDescription("nkdcdnc");
        orderCertificate.setGiftCertificate(giftCertificate);
        orderCertificate.setAmount(1);
        orderCertificate.setOrder(order);
        order.setOrderCertificates(List.of(orderCertificate));
        Order actual = orderRepository.save(order);
        assertNotNull(actual);
    }

//    @Test
//    void findTotalUserOrdersCount(){
//        int actual = orderRepository.findCountAllRecords(1L);
//        int expected = 1;
//        assertEquals(expected, actual);
//    }

    @Test
    void findUserOrderList(){
        List<Order> orders = orderRepository.findOrdersByUser_UserId(1L, PageRequest.of(0, 1));
        int expected = 1;
        assertEquals(expected, orders.size());
    }

    @Test
    void findOrderByIdTest(){
        Order order = orderRepository.findOrderByOrderId(1L);
        assertNotNull(order);
    }
}
