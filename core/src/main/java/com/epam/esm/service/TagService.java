package com.epam.esm.service;

import com.epam.esm.model.dto.TagDto;

import java.util.List;

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
    List<TagDto> showAll(int limit, int offset);

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
    TagDto showById(long id);

    List<TagDto> showWidelyUserTagWithHighestOrdersCost();

    int countAllTags();
}
