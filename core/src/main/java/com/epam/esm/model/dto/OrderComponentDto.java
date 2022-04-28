package com.epam.esm.model.dto;

import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.hateoas.RepresentationModel;

public class OrderComponentDto extends RepresentationModel<OrderComponentDto> {

    private GiftCertificate giftCertificate;
    private int amount;

    public OrderComponentDto(){}

    public OrderComponentDto(GiftCertificate giftCertificate, int amount) {
        this.giftCertificate = giftCertificate;
        this.amount = amount;
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
        final StringBuilder sb = new StringBuilder("OrderComponentDto{");
        sb.append("giftCertificate=").append(giftCertificate);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
