package com.epam.esm.repository.impl;

import com.epam.esm.domain.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.mapper.CertificateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
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
            SELECT gift_certificate.gift_certificate_id, gift_certificate.name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            JOIN tags_gift_certificates ON tags_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
            JOIN tag ON tags_gift_certificates.tag_id = tag.tag_id
            WHERE tag.name=?""";
    private static final String SQL_SORT_ASC_BY_DATE = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY create_date
            """;
    private static final String SQL_SORT_DESC_BY_DATE = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY create_date DESC
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
            ORDER BY name DESC""";
    private static final String SQL_INSERT_GENERATED_KEYS = """
            INSERT INTO tags_gift_certificates(tag_id, gift_certificate_id)
            VALUES (?,?)""";
    private static final String SQL_BOTH_SORTING = """
            SELECT gift_certificate_id, name, description, price, duration,
            create_date, last_update_date FROM gift_certificate
            ORDER BY name, create_date;
            """;
    private static final String END_PART = "%'";
    private static final long RETURN_VALUE = -1L;
    private static final int RETURN_ROW_VALUE = 1;

    private final JdbcOperations jdbcOperations;

    @Autowired
    public CertificateRepositoryImpl(JdbcOperations jdbcOperations){
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement.setString(1, giftCertificate.getName());
                    preparedStatement.setString(2, giftCertificate.getDescription());
                    preparedStatement.setBigDecimal(3, giftCertificate.getPrice());
                    preparedStatement.setInt(4, giftCertificate.getDuration());
                    preparedStatement.setTimestamp(5, Timestamp.valueOf(giftCertificate.getCreateDate()));
                    preparedStatement.setTimestamp(6, Timestamp.valueOf(giftCertificate.getLastUpdateDate()));
                    return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : RETURN_VALUE;
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
    public boolean update(long id, GiftCertificate giftCertificate) {
        return jdbcOperations.update(SQL_UPDATE,
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration(),
                giftCertificate.getCreateDate(),
                giftCertificate.getLastUpdateDate(),
                id) == RETURN_ROW_VALUE;
    }

    @Override
    public boolean delete(long id) {
        return jdbcOperations.update(SQL_DELETE, id) == RETURN_ROW_VALUE;
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
        StringBuilder builder = new StringBuilder(SQL_SELECT_BY_PART_DESCRIPTION);
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

    @Override
    public List<GiftCertificate> bothSorting() {
        return jdbcOperations.query(SQL_BOTH_SORTING, new CertificateMapper());
    }

    @Override
    public boolean insertKeys(long tagId, long certificateId) {
        return jdbcOperations.update(SQL_INSERT_GENERATED_KEYS, tagId, certificateId) > RETURN_ROW_VALUE;
    }
}
