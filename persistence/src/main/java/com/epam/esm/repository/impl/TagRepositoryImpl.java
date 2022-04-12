package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.mapper.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Tag repository implements methods of the TagRepository
 * interface. The class is annotated with as a repository, which qualifies it to be
 * automatically created by component-scanning.
 * @author Anna Merkul.
 */
@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String SQL_SELECT = "SELECT tag_id, name FROM tag";
    private static final String SQL_INSERT = "INSERT INTO tag(name) VALUES (?)";
    private static final String SQL_SELECT_BY_ID = "SELECT tag_id, name FROM tag WHERE tag_id=?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM tag WHERE tag_id=?";
    private static final String SQL_SELECT_BY_CERTIFICATE_ID = """
            SELECT tag.tag_id, tag.name FROM tag
            JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id
            JOIN gift_certificate ON tags_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
            WHERE gift_certificate.gift_certificate_id=?""";
    private static final long RETURN_VALUE = -1L;
    private static final int RETURN_ROW_VALUE = 1;

    private final JdbcOperations jdbcOperations;

    /**
     * Instantiates a new Tag repository. JdbcOperations is an interface
     * which is implemented by JdbcTemplate class.
     *
     * @param jdbcOperations the jdbc operations
     */
    @Autowired
    public TagRepositoryImpl(JdbcOperations jdbcOperations){
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, tag.getName());
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().longValue() : RETURN_VALUE;
    }

    @Override
    public Set<Tag> show() {
        return new HashSet<>(jdbcOperations.query(SQL_SELECT, new TagMapper()));
    }

    @Override
    public Tag showById(long id) {
        return jdbcOperations.query(SQL_SELECT_BY_ID, new TagMapper(), id)
                .stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public boolean delete(long id) {
        return jdbcOperations.update(SQL_DELETE_BY_ID, id) == RETURN_ROW_VALUE;
    }

    @Override
    public Set<Tag> showByCertificateId(long certificateId) {
        return new HashSet<>(jdbcOperations.query(SQL_SELECT_BY_CERTIFICATE_ID, new TagMapper(), certificateId));
    }
}
