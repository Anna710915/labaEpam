package com.epam.esm.repository.mapper;

import com.epam.esm.domain.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The type Certificate mapper implements PowMapper class and used for mapping rows of the
 * ResultSet.
 * @author Anna Merkul
 */
public class CertificateMapper implements RowMapper<GiftCertificate> {
    /**
     * The constant GIFT_CERTIFICATE_ID.
     */
    public static final String GIFT_CERTIFICATE_ID = "gift_certificate_id";
    /**
     * The constant CERTIFICATE_NAME.
     */
    public static final String CERTIFICATE_NAME = "name";
    /**
     * The constant DESCRIPTION.
     */
    public static final String DESCRIPTION = "description";
    /**
     * The constant PRICE.
     */
    public static final String PRICE = "price";
    /**
     * The constant DURATION.
     */
    public static final String DURATION = "duration";
    /**
     * The constant CREATE_DATE.
     */
    public static final String CREATE_DATE = "create_date";
    /**
     * The constant LAST_UPDATE_DATE.
     */
    public static final String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) {
        GiftCertificate giftCertificate = new GiftCertificate();
        try {
            giftCertificate.setId(rs.getLong(GIFT_CERTIFICATE_ID));
            giftCertificate.setName(rs.getString(CERTIFICATE_NAME));
            giftCertificate.setDescription(rs.getString(DESCRIPTION));
            giftCertificate.setPrice(rs.getBigDecimal(PRICE));
            giftCertificate.setDuration(rs.getInt(DURATION));
            giftCertificate.setCreateDate(rs.getTimestamp(CREATE_DATE).toLocalDateTime());
            giftCertificate.setLastUpdateDate(rs.getTimestamp(LAST_UPDATE_DATE).toLocalDateTime());
        }catch (SQLException exception) {
            giftCertificate = null;
        }
        return giftCertificate;
    }
}
