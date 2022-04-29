package com.epam.esm.service.impl;

import com.epam.esm.model.dto.OrderComponentDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderCertificate;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long createOrder(OrderDto orderDto) {
        Order order = new Order(orderDto.getOrderCost(), new User(orderDto.getUserId()));
        order.setOrderDate(LocalDateTime.now());
        OrderCertificate orderCertificate = new OrderCertificate();
        List<OrderComponentDto> orderComponentDtos = orderDto.getOrderComponentDtos();
        List<OrderCertificate> orderCertificates = new ArrayList<>();
        for(OrderComponentDto orderComponentDto : orderComponentDtos){
            orderCertificate.setGiftCertificate(orderComponentDto.getGiftCertificate());
            orderCertificate.setAmount(orderComponentDto.getAmount());
            orderCertificate.setOrder(order);
            orderCertificates.add(orderCertificate);
        }
        order.setOrderCertificates(orderCertificates);
        return orderRepository.createOrder(order);
    }

    @Override
    @Transactional
    public List<OrderDto> findPaginatedUserOrders(long userId, int size, int offset) {
        List<Order> orders = orderRepository.selectPaginatedUserOrders(userId, size, offset);
        return buildOrderDtoList(orders);
    }

    @Override
    @Transactional
    public OrderDto findUserOrder(long orderId) {
        Order order = orderRepository.findUserOrder(orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderCost(order.getOrderCost());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setUserId(order.getUser().getUserId());
        orderDto.setOrderComponentDtos(buildOrderComponentDto(order.getOrderCertificates()));
        return orderDto;
    }

    @Override
    public int findTotalRecords(long userId) {
       return orderRepository.findCountAllRecords(userId);
    }

    private List<OrderDto> buildOrderDtoList(List<Order> orders){
        List<OrderDto> orderDtos = new ArrayList<>();
        for(Order order : orders){
            OrderDto orderDto = new OrderDto();
            orderDto.setOrderId(order.getOrderId());
            orderDto.setOrderCost(order.getOrderCost());
            orderDto.setOrderDate(order.getOrderDate());
            orderDto.setUserId(order.getUser().getUserId());
            orderDto.setOrderComponentDtos(buildOrderComponentDto(order.getOrderCertificates()));
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    private List<OrderComponentDto> buildOrderComponentDto(List<OrderCertificate> orderCertificates){
        List<OrderComponentDto> orderComponentDtos = new ArrayList<>();
        for(OrderCertificate orderCertificate : orderCertificates){
            OrderComponentDto orderComponentDto = new OrderComponentDto();
            orderComponentDto.setGiftCertificate(orderCertificate.getGiftCertificate());
            orderComponentDto.getGiftCertificate().setTags(orderCertificate.getGiftCertificate().getTags());
            orderComponentDto.setAmount(orderCertificate.getAmount());
            orderComponentDtos.add(orderComponentDto);
        }
        return orderComponentDtos;
    }
}
