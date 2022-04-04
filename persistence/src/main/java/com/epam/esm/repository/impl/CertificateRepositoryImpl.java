package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.mapper.CertificateMapper;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String SQL_INSERT = """ 
            INSERT INTO gift_certificate(name, description, price, duration,
            create_date, last_update_date) VALUES(?,?,?,?,?,?)
            """;
    private static final String SQL_SELECT = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate""";
    private static final String SQL_SELECT_BY_ID = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate 
            WHERE gift_certificate_id=?
            """;
    private static final String SQL_UPDATE = """
            UPDATE gift_certificate
            SET name=?, description=?, price=?, duration=?,
            create_date=?, last_update_date=?
            WHERE gift_certificate_id=?
            """;
    private static final String SQL_DELETE = """
            DELETE FROM gift_certificate WHERE gift_certificate_id=?""";
    private static final String SQL_SELECT_BY_TAG_NAME = """
            SELECT gift_certificate.gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            JOIN tags_gift_certificates ON tags_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
            JOIN tag ON tags_gift_certificates.tag_id = tag.tag_id
            WHERE tag.name=?""";
    private static final String SQL_SORT_ASC_BY_DATE = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY create_date;
            """;
    private static final String SQL_SORT_DESC_BY_DATE = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY DESC create_date;
            """;
    private static final String SQL_SELECT_BY_PART_NAME = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            WHERE name LIKE '%""";
    private static final String SQL_SELECT_BY_PART_DESCRIPTION = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            WHERE description LIKE '%""";
    private static final String SQL_SORT_ASC_BY_NAME = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY name""";
    private static final String SQL_SORT_DESC_BY_NAME = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY DESC name
            """;
    private static final String END_PART = "%'";

    private JdbcOperations jdbcOperations;

    public CertificateRepositoryImpl(JdbcOperations jdbcOperations){
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public void create(GiftCertificate giftCertificate) {
        jdbcOperations.update(SQL_INSERT,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate());
    }

    @Override
    public List<GiftCertificate> show() {
        return jdbcOperations.query(SQL_SELECT, new CertificateMapper());
    }

    @Override
    public GiftCertificate showById(long id) {
        return jdbcOperations.query(SQL_SELECT_BY_ID, new CertificateMapper(), id)
                .stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public void update(long id, GiftCertificate giftCertificate) {
        jdbcOperations.update(SQL_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate(),
                id);
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update(SQL_DELETE, id);
    }

    @Override
    public List<GiftCertificate> showByTagName(String name) {
        return  jdbcOperations.query(SQL_SELECT_BY_TAG_NAME, new CertificateMapper(), name);
    }

    @Override
    public List<GiftCertificate> showByPartName(String partName) {
        StringBuilder builder = new StringBuilder(SQL_SELECT_BY_PART_NAME);
        builder.append(partName).append(END_PART);
        return jdbcOperations.query(builder.toString(), new CertificateMapper());
    }

    @Override
    public List<GiftCertificate> showByPartDescription(String partDescription) {
        StringBuilder builder = new StringBuilder(SQL_SELECT_BY_PART_NAME);
        builder.append(partDescription).append(END_PART);
        return jdbcOperations.query(builder.toString(), new CertificateMapper());
    }

    @Override
    public List<GiftCertificate> sortByDateAsc() {
        return jdbcOperations.query(SQL_SORT_ASC_BY_DATE, new CertificateMapper());
    }

    @Override
    public List<GiftCertificate> sortByDateDesc() {
        return jdbcOperations.query(SQL_SORT_DESC_BY_DATE, new CertificateMapper());
    }

    @Override
    public List<GiftCertificate> sortByNameAsc() {
        return jdbcOperations.query(SQL_SORT_ASC_BY_NAME, new CertificateMapper());
    }

    @Override
    public List<GiftCertificate> sortByNameDesc() {
        return jdbcOperations.query(SQL_SORT_DESC_BY_NAME, new CertificateMapper());
    }
}
