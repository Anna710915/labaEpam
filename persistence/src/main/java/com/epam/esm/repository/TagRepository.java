package com.epam.esm.repository;

import com.epam.esm.domain.Tag;
import java.util.Set;

/**
 * The interface Tag repository contains methods for tags.
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
     * @return the set
     */
    Set<Tag> show();

    /**
     * Show by id tag.
     *
     * @param id the id
     * @return the tag
     */
    Tag showById(long id);

    /**
     * Delete a tag.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(long id);

    /**
     * Show by certificate id the set of tags.
     *
     * @param certificateId the certificate id
     * @return the set
     */
    Set<Tag> showByCertificateId(long certificateId);
}
