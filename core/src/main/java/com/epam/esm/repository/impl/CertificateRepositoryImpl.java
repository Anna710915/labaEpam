package com.epam.esm.repository.impl;

import com.epam.esm.model.dto.SortDto;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repository.QueryCertificateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;

import static com.epam.esm.repository.query.CertificateQuery.*;

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

    private static final String SORT_BY_NAME_ASC = "asc_name";
    private static final String SORT_BY_NAME_DESC = "desc_name";
    private static final String SORT_BY_DATE_ASC = "asc_date";
    private static final String SORT_BY_DATE_DESC = "desc_date";
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
    public List<GiftCertificate> findAllCertificates(int limit, int offset, String part, List<String> tagsName, SortDto sort) {
        String query = FIND_ALL_CERTIFICATES;
        if(!tagsName.isEmpty()){
            query += "(" + buildFindByTagsQuery(tagsName) + ") AS tags_query ";
        }else {
            query += "gift_certificate ";
        }

        if (part != null){
            query += "WHERE name LIKE '%" + part + "%' OR description LIKE '%" + part + "%' ";
        }

        if(sort.getName() != null || sort.getDate() != null){
            query += "ORDER BY ";
            if(SORT_BY_NAME_ASC.equals(sort.getName())){
                query += "name";
            }else if(SORT_BY_NAME_DESC.equals(sort.getName())){
                query += "name DESC";
            }
            if(sort.getName() != null && sort.getDate() != null){
                query += " , ";
            }
            if(SORT_BY_DATE_ASC.equals(sort.getDate())){
                query += "last_update_date";
            }else if(SORT_BY_DATE_DESC.equals(sort.getDate())){
                query += "last_update_date DESC";
            }
        }

        Query certificateQuery = entityManager.createNativeQuery(query, GiftCertificate.class)
                .setMaxResults(limit).setFirstResult(offset);
        return (List<GiftCertificate>) certificateQuery.getResultList();
    }

    @Override
    public int findCountRecords(String part, List<String> tagsName, SortDto sort) {
        String query = FIND_ALL_CERTIFICATES;
        if(!tagsName.isEmpty()){
            query += "(" + buildFindByTagsQuery(tagsName) + ") AS tags_query ";
        }else {
            query += "gift_certificate ";
        }

        if (part != null){
            query += "WHERE name LIKE '%" + part + "%' OR description LIKE '%" + part + "%' ";
        }

        if(sort.getName() != null || sort.getDate() != null){
            query += "ORDER BY ";
            if(SORT_BY_NAME_ASC.equals(sort.getName())){
                query += "name";
            }else if(SORT_BY_NAME_DESC.equals(sort.getName())){
                query += "name DESC";
            }
            if(sort.getName() != null && sort.getDate() != null){
                query += " , ";
            }
            if(SORT_BY_DATE_ASC.equals(sort.getDate())){
                query += "last_update_date";
            }else if(SORT_BY_DATE_DESC.equals(sort.getDate())){
                query += "last_update_date DESC";
            }
        }

        Query certificateQuery = entityManager.createNativeQuery(query, GiftCertificate.class);
        return certificateQuery.getResultList().size();
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
