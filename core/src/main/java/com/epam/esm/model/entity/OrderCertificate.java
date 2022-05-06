package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;

import javax.persistence.*;

@EntityListeners(AuditListener.class)
@Entity
@Table(name = "orders_gift_certificates")
public class OrderCertificate{

    @Id
    @Column(name = "orders_gift_certificates_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderGiftCertificateId;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "gift_certificate_id")
    private GiftCertificate giftCertificate;

    @Column(name = "amount")
    private int amount;

    public OrderCertificate() {}

    public OrderCertificate(long orderGiftCertificateId, Order order, GiftCertificate giftCertificate, int amount) {
        this.orderGiftCertificateId = orderGiftCertificateId;
        this.order = order;
        this.giftCertificate = giftCertificate;
        this.amount = amount;
    }

    public long getOrderGiftCertificateId() {
        return orderGiftCertificateId;
    }

    public void setOrderGiftCertificateId(long orderGiftCertificateId) {
        this.orderGiftCertificateId = orderGiftCertificateId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderCertificate that = (OrderCertificate) o;

        if (orderGiftCertificateId != that.orderGiftCertificateId) return false;
        if (amount != that.amount) return false;
        if (order != null ? !order.equals(that.order) : that.order != null) return false;
        return giftCertificate != null ? giftCertificate.equals(that.giftCertificate) : that.giftCertificate == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (orderGiftCertificateId ^ (orderGiftCertificateId >>> 32));
        result = 31 * result + (order != null ? order.hashCode() : 0);
        result = 31 * result + (giftCertificate != null ? giftCertificate.hashCode() : 0);
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderCertificate{");
        sb.append("orderGiftCertificateId=").append(orderGiftCertificateId);
        sb.append(", order=").append(order);
        sb.append(", giftCertificate=").append(giftCertificate);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}