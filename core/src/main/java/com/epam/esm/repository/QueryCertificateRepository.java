package com.epam.esm.repository;

import com.epam.esm.model.entity.GiftCertificate;

import java.util.List;

public interface QueryCertificateRepository {

    /**
     * Find count by tags query int.
     *
     * @param tagsName the tags name
     * @return the int
     */
    int findCountByTagsQuery(List<String> tagsName);

    /**
     * Find by tags list.
     *
     * @param limit    the limit
     * @param offset   the offset
     * @param tagsName the tags name
     * @return the list
     */
    List<GiftCertificate> findByTags(int limit, int offset, List<String> tagsName);
}
