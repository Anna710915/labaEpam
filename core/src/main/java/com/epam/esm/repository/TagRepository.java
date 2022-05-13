package com.epam.esm.repository;

import com.epam.esm.model.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository contains methods for tags.
 *
 * @author Anna Merkul
 */
public interface TagRepository {
    /**
     * Create tags.
     *
     * @param tag the tag
     * @return the long
     */
    long create(Tag tag);

    /**
     * Show the set of tags.
     *
     * @param limit  the limit
     * @param offset the offset
     * @return the set
     */
    List<Tag> show(int limit, int offset);

    /**
     * Show by id tag.
     *
     * @param id the id
     * @return the tag
     */
    Tag showById(long id);

    /**
     * Show by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<Tag> showByName(String name);

    /**
     * Delete a tag.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(long id);

    /**
     * Find widely user tag with highest orders cost list.
     *
     * @return the list
     */
    List<Tag> findWidelyUserTagWithHighestOrdersCost();

    /**
     * Count all tags int.
     *
     * @return the int
     */
    int countAllTags();
}
