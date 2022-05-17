package com.epam.esm.repository.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repository.QueryCertificateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

/**
 * The type Certificate repository implements methods of the CertificateRepository
 * interface. The class is annotated with as a repository, which qualifies it to be
 * automatically created by component-scanning.
 * @author Anna Merkul.
 */
@Repository
@Transactional
public class CertificateRepositoryImpl  implements QueryCertificateRepository {

    private static final String SQL_FIND_BY_TAGS_START = """
            SELECT gift_certificate_id, name, description, price, duration, create_date, last_update_date  FROM (
                            SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
                            JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id\s
                            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
                            WHERE tag.name = '""";
    private static final String SQL_FIND_BY_TAGS_UNION = """
            '
                            UNION SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
                            JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
                            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
                            WHERE tag.name= '""";
    private static final String SQL_FIND_BY_TAGS_END = """
            ') AS used_tags_certificates
                                   GROUP BY gift_certificate_id
                                   HAVING COUNT(tag_name) =""";

    private static final int FIRST_TAG_INDEX = 0;

    private final EntityManager entityManager;

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
