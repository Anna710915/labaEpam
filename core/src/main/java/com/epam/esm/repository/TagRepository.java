package com.epam.esm.repository;

import com.epam.esm.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.epam.esm.repository.query.TagQuery.COUNT_ALL_TAGS;
import static com.epam.esm.repository.query.TagQuery.FIND_WIDELY_USER_TAG_WITH_HIGHEST_ORDERS_COST;

/**
 * The interface Tag repository contains methods for tags.
 *
 * @author Anna Merkul
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Find tag by name tag.
     *
     * @param name the name
     * @return the tag
     */
    Tag findTagByName(String name);

    /**
     * Find tag by id tag.
     *
     * @param id the id
     * @return the tag
     */
    Tag findTagById(long id);

    /**
     * Delete a tag.
     *
     * @param id the id
     * @return the boolean
     */
    int deleteTagById(long id);

    /**
     * Find widely user tag with highest orders cost list.
     *
     * @return the list
     */
    @Query(value = FIND_WIDELY_USER_TAG_WITH_HIGHEST_ORDERS_COST, nativeQuery = true)
    List<Tag> findWidelyUserTagWithHighestOrdersCost();

    /**
     * Count all tags int.
     *
     * @return the int
     */
    @Query(value = COUNT_ALL_TAGS, nativeQuery = true)
    int countAllTags();
}
