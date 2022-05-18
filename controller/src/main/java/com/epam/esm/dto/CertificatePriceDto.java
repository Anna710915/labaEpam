package com.epam.esm.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * The type Certificate price dto.
 */
public class CertificatePriceDto {

    @Min(value = 5, message = "{min_certificate_price}")
    @Max(value = 100000, message = "{max_certificate_price}")
    private BigDecimal price;

    /**
     * Instantiates a new Certificate price dto.
     */
    public CertificatePriceDto(){}

    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificatePriceDto that = (CertificatePriceDto) o;

        return price != null ? price.equals(that.price) : that.price == null;
    }

    @Override
    public int hashCode() {
        return price != null ? price.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CertificatePriceDto{");
        sb.append("price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}
