package com.epam.esm.service;

import com.epam.esm.domain.Tag;

import java.util.Set;

/**
 * The interface Tag service contains methods for business logic with tags.
 * @author Anna Merkul
 */
public interface TagService {
    /**
     * Show all set of tags.
     *
     * @return the set
     */
    Set<Tag> showAll();

    /**
     * Delete a tag.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(long id);

    /**
     * Show by id tag.
     *
     * @param id the id
     * @return the tag
     */
    Tag showById(long id);
}
