package com.epam.esm.repository;

import com.epam.esm.model.dto.SortDto;
import com.epam.esm.model.entity.GiftCertificate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static com.epam.esm.repository.query.CertificateQuery.FIND_COUNT_RECORDS;

/**
 * The interface Query certificate repository.
 */
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

    List<GiftCertificate> findAllCertificates (int limit, int offset, String part, List<String> tagsName, SortDto sort);


    int findCountRecords(String part, List<String> tagsName, SortDto sort);
}
