package com.epam.esm.service.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.dto.OrderComponentDto;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderCertificate;
import com.epam.esm.model.dto.OrderDto;
import com.epam.esm.model.entity.User;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.util.MessageLanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final MessageLanguageUtil messageLanguageUtil;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            MessageLanguageUtil messageLanguageUtil){
        this.orderRepository = orderRepository;
        this.messageLanguageUtil = messageLanguageUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long createOrder(OrderDto orderDto) {
        Order order = new Order(orderDto.getOrderCost(), new User(orderDto.getUserId()));
        order.setOrderDate(LocalDateTime.now());
        List<OrderComponentDto> orderComponentDtos = orderDto.getOrderComponentDtos();
        List<OrderCertificate> orderCertificates = new ArrayList<>();
        for(OrderComponentDto orderComponentDto : orderComponentDtos){
            OrderCertificate orderCertificate = new OrderCertificate();
            orderCertificate.setGiftCertificate(orderComponentDto.getGiftCertificate());
            orderCertificate.setAmount(orderComponentDto.getAmount());
            orderCertificate.setOrder(order);
            orderCertificates.add(orderCertificate);
        }
        order.setOrderCertificates(orderCertificates);
        return orderRepository.save(order).getOrderId();
    }

    @Override
    @Transactional
    public List<OrderDto> findPaginatedUserOrders(long userId, int page, int size) {
        List<Order> orders = orderRepository.findOrdersByUser_UserId(userId, PageRequest.of(page - 1, size));
        return buildOrderDtoList(orders);
    }

    @Override
    @Transactional
    public OrderDto findUserOrder(long orderId) {
        Order order = orderRepository.findOrderByOrderId(orderId);
        checkOrder(order, orderId);
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setOrderCost(order.getOrderCost());
        orderDto.setOrderDate(order.getOrderDate());
        orderDto.setUserId(order.getUser().getUserId());
        orderDto.setOrderComponentDtos(buildOrderComponentDto(order.getOrderCertificates()));
        return orderDto;
    }

    @Override
    @Transactional
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

    private void checkOrder(Order order, long orderId){
        if(order == null){
            throw new CustomNotFoundException(messageLanguageUtil.getMessage("not_found.order") + orderId);
        }
    }
}
