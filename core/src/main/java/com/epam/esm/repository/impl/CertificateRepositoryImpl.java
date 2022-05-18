package com.epam.esm.repository.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repository.QueryCertificateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import static com.epam.esm.repository.query.CertificateQuery.SQL_FIND_BY_TAGS_START;
import static com.epam.esm.repository.query.CertificateQuery.SQL_FIND_BY_TAGS_UNION;
import static com.epam.esm.repository.query.CertificateQuery.SQL_FIND_BY_TAGS_END;

/**
 * The type Certificate repository implements methods of the CertificateRepository
 * interface. The class is annotated with as a repository, which qualifies it to be
 * automatically created by component-scanning.
 *
 * @author Anna Merkul.
 */
@Repository
@Transactional
public class CertificateRepositoryImpl  implements QueryCertificateRepository {

    private static final int FIRST_TAG_INDEX = 0;

    private final EntityManager entityManager;

    /**
     * Instantiates a new Certificate repository.
     *
     * @param entityManager the entity manager
     */
    @Autowired
    public CertificateRepositoryImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public int findCountByTagsQuery(List<String> tagsName) {
        String query = buildFindByTagsQuery(tagsName);
        Query certificateNativeQuery = entityManager.createNativeQuery(query, GiftCertificate.class);
        return certificateNativeQuery.getResultList().size();
    }

    @Override
    public List<GiftCertificate> findByTags(int limit, int offset, List<String> tagsName) {
        String query = buildFindByTagsQuery(tagsName);
        Query certificateQuery = entityManager.createNativeQuery(query, GiftCertificate.class)
                .setMaxResults(limit).setFirstResult(offset);
        return (List<GiftCertificate>) certificateQuery.getResultList();
    }

    private String buildFindByTagsQuery(List<String> tagsName){
        StringBuilder builder = new StringBuilder(SQL_FIND_BY_TAGS_START);
        int tagSize = tagsName.size();
        builder.append(tagsName.get(FIRST_TAG_INDEX));
        tagsName.remove(FIRST_TAG_INDEX);
        for(String name : tagsName){
            builder.append(SQL_FIND_BY_TAGS_UNION).append(name);
        }
        builder.append(SQL_FIND_BY_TAGS_END).append(tagSize);
        return builder.toString();
    }
}
