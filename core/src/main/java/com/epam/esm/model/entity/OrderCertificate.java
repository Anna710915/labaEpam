package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;

import javax.persistence.*;

/**
 * The type Order certificate. This is a linking table for users orders and
 * gift certificates. It consists an amount gift certificates field.
 *
 * @author Anna Merkul
 */
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

    /**
     * Instantiates a new Order certificate.
     */
    public OrderCertificate() {}

    /**
     * Instantiates a new Order certificate.
     *
     * @param orderGiftCertificateId the order gift certificate id
     * @param order                  the order
     * @param giftCertificate        the gift certificate
     * @param amount                 the amount
     */
    public OrderCertificate(long orderGiftCertificateId, Order order, GiftCertificate giftCertificate, int amount) {
        this.orderGiftCertificateId = orderGiftCertificateId;
        this.order = order;
        this.giftCertificate = giftCertificate;
        this.amount = amount;
    }

    /**
     * Gets order gift certificate id.
     *
     * @return the order gift certificate id
     */
    public long getOrderGiftCertificateId() {
        return orderGiftCertificateId;
    }

    /**
     * Sets order gift certificate id.
     *
     * @param orderGiftCertificateId the order gift certificate id
     */
    public void setOrderGiftCertificateId(long orderGiftCertificateId) {
        this.orderGiftCertificateId = orderGiftCertificateId;
    }

    /**
     * Gets order.
     *
     * @return the order
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Sets order.
     *
     * @param order the order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Gets gift certificate.
     *
     * @return the gift certificate
     */
    public GiftCertificate getGiftCertificate() {
        return giftCertificate;
    }

    /**
     * Sets gift certificate.
     *
     * @param giftCertificate the gift certificate
     */
    public void setGiftCertificate(GiftCertificate giftCertificate) {
        this.giftCertificate = giftCertificate;
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets amount.
     *
     * @param amount the amount
     */
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
        return "OrderCertificate{" + "orderGiftCertificateId=" + orderGiftCertificateId +
                ", order=" + order +
                ", giftCertificate=" + giftCertificate +
                ", amount=" + amount +
                '}';
    }
}