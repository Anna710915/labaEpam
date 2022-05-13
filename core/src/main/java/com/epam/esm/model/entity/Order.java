package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Order.
 * @author Anna Merkul
 */
@EntityListeners(AuditListener.class)
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(name = "order_cost")
    private BigDecimal orderCost;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderCertificate> orderCertificates = new ArrayList<>();

    /**
     * Instantiates a new Order.
     */
    public Order(){}

    /**
     * Instantiates a new Order.
     *
     * @param orderCost the order cost
     * @param user      the user
     */
    public Order(BigDecimal orderCost, User user){
        this.orderCost = orderCost;
        this.user = user;
    }

    /**
     * Instantiates a new Order.
     *
     * @param orderId   the order id
     * @param orderCost the order cost
     * @param orderDate the order date
     */
    public Order(long orderId, BigDecimal orderCost, LocalDateTime orderDate){
        this.orderId = orderId;
        this.orderCost = orderCost;
        this.orderDate = orderDate;
    }

    /**
     * Instantiates a new Order.
     *
     * @param orderId           the order id
     * @param orderCost         the order cost
     * @param orderDate         the order date
     * @param user              the user
     * @param orderCertificates the order certificates
     */
    public Order(long orderId, BigDecimal orderCost, LocalDateTime orderDate,
                 User user, List<OrderCertificate> orderCertificates) {
        this.orderId = orderId;
        this.orderCost = orderCost;
        this.orderDate = orderDate;
        this.user = user;
        this.orderCertificates = orderCertificates;
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
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets order certificates.
     *
     * @return the order certificates
     */
    public List<OrderCertificate> getOrderCertificates() {
        return orderCertificates;
    }

    /**
     * Sets order certificates.
     *
     * @param orderCertificates the order certificates
     */
    public void setOrderCertificates(List<OrderCertificate> orderCertificates) {
        this.orderCertificates = orderCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (orderCost != null ? !orderCost.equals(order.orderCost) : order.orderCost != null) return false;
        if (orderDate != null ? !orderDate.equals(order.orderDate) : order.orderDate != null) return false;
        if (user != null ? !user.equals(order.user) : order.user != null) return false;
        return orderCertificates != null ? orderCertificates.equals(order.orderCertificates) : order.orderCertificates == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + (orderCost != null ? orderCost.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (orderCertificates != null ? orderCertificates.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("orderId=").append(orderId);
        sb.append(", orderCost=").append(orderCost);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", user=").append(user);
        sb.append(", orderCertificates=").append(orderCertificates);
        sb.append('}');
        return sb.toString();
    }
}
