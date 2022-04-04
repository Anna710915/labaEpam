package com.epam.esm.repository;

import com.epam.esm.domain.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository {
    void create(Tag tag);
    List<Tag> show();
    Tag showById(long id);
    void delete(long id);
}
