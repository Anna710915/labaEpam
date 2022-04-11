package com.epam.esm.service;

import com.epam.esm.domain.Tag;

import java.util.Set;

public interface TagService {
    Set<Tag> showAll();
    boolean delete(long id);
    Tag showById(long id);
}
