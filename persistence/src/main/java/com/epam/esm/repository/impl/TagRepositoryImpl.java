package com.epam.esm.repository.impl;

import com.epam.esm.domain.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.mapper.TagMapper;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;
import java.util.Optional;

public class TagRepositoryImpl implements TagRepository {

    private JdbcOperations jdbcOperations;

    public TagRepositoryImpl(JdbcOperations jdbcOperations){
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public void create(Tag tag) {
        jdbcOperations.update("INSERT INTO tag(name) VALUES (?)", tag.getName());
    }

    @Override
    public List<Tag> show() {
        return jdbcOperations.query("SELECT tag_id, name FROM tag", new TagMapper());
    }

    @Override
    public Tag showById(long id) {
        return jdbcOperations.query("SELECT tag_id, name FROM tag", new TagMapper())
                .stream()
                .findAny()
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        jdbcOperations.update("DELETE FROM tag WHERE tag_id=?", id);
    }
}
