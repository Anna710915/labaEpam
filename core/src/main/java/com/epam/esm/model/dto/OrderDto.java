package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Order dto.
 * @author Anna Merkul
 */
public class OrderDto extends RepresentationModel<OrderDto> {
    private long orderId;
    private BigDecimal orderCost;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime orderDate;

    private List<OrderComponentDto> orderComponentDtos;
    private long userId;

    /**
     * Instantiates a new Order dto.
     */
    public OrderDto(){}

    /**
     * Instantiates a new Order dto.
     *
     * @param orderCost          the order cost
     * @param orderComponentDtos the order component dtos
     * @param userId             the user id
     */
    public OrderDto(BigDecimal orderCost, List<OrderComponentDto> orderComponentDtos, long userId){
        this.orderCost = orderCost;
        this.userId = userId;
        this.orderComponentDtos = orderComponentDtos;
    }

    /**
     * Gets order id.
     *
     * @return the order id
     */
    public long getOrderId() {
        return orderId;
    }

    /**
     * Sets order id.
     *
     * @param orderId the order id
     */
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets order cost.
     *
     * @return the order cost
     */
    public BigDecimal getOrderCost() {
        return orderCost;
    }

    /**
     * Sets order cost.
     *
     * @param orderCost the order cost
     */
    public void setOrderCost(BigDecimal orderCost) {
        this.orderCost = orderCost;
    }

    /**
     * Gets order date.
     *
     * @return the order date
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Sets order date.
     *
     * @param orderDate the order date
     */
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Gets order component dtos.
     *
     * @return the order component dtos
     */
    public List<OrderComponentDto> getOrderComponentDtos() {
        return orderComponentDtos;
    }

    /**
     * Sets order component dtos.
     *
     * @param orderComponentDtos the order component dtos
     */
    public void setOrderComponentDtos(List<OrderComponentDto> orderComponentDtos) {
        this.orderComponentDtos = orderComponentDtos;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderDto orderDto = (OrderDto) o;

        if (orderId != orderDto.orderId) return false;
        if (userId != orderDto.userId) return false;
        if (orderCost != null ? !orderCost.equals(orderDto.orderCost) : orderDto.orderCost != null) return false;
        if (orderDate != null ? !orderDate.equals(orderDto.orderDate) : orderDto.orderDate != null) return false;
        return orderComponentDtos != null ? orderComponentDtos.equals(orderDto.orderComponentDtos) : orderDto.orderComponentDtos == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + (orderCost != null ? orderCost.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (orderComponentDtos != null ? orderComponentDtos.hashCode() : 0);
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderDto{");
        sb.append("orderId=").append(orderId);
        sb.append(", orderCost=").append(orderCost);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", orderComponentDtos=").append(orderComponentDtos);
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
