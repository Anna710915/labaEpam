package com.epam.esm.service;

import com.epam.esm.model.dto.TagDto;

import java.util.List;

/**
 * The interface Tag service contains methods for business logic with tags.
 *
 * @author Anna Merkul
 */
public interface TagService {

    List<TagDto> showAll(int page, int size);

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

    /**
     * Show widely user tag with highest orders cost list.
     *
     * @return the list
     */
    List<TagDto> showWidelyUserTagWithHighestOrdersCost();

    /**
     * Count all tags int.
     *
     * @return the int
     */
    int countAllTags();
}
