package com.epam.esm.model.dto;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.hateoas.RepresentationModel;

/**
 * The type Order component dto.
 * @author Anna Merkul
 */
public class OrderComponentDto extends RepresentationModel<OrderComponentDto> {

    private GiftCertificate giftCertificate;
    private int amount;

    /**
     * Instantiates a new Order component dto.
     */
    public OrderComponentDto(){}

    /**
     * Instantiates a new Order component dto.
     *
     * @param giftCertificate the gift certificate
     * @param amount          the amount
     */
    public OrderComponentDto(GiftCertificate giftCertificate, int amount) {
        this.giftCertificate = giftCertificate;
        this.amount = amount;
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
        if (!super.equals(o)) return false;

        OrderComponentDto that = (OrderComponentDto) o;

        if (amount != that.amount) return false;
        return giftCertificate != null ? giftCertificate.equals(that.giftCertificate) : that.giftCertificate == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (giftCertificate != null ? giftCertificate.hashCode() : 0);
        result = 31 * result + amount;
        return result;
    }

    @Override
    public String toString() {
        return "OrderComponentDto{" + "giftCertificate=" + giftCertificate +
                ", amount=" + amount +
                '}';
    }
}
