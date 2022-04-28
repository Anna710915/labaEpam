package com.epam.esm.repository.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.util.MessageLanguageUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import org.hibernate.query.Query;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The type Certificate repository implements methods of the CertificateRepository
 * interface. The class is annotated with as a repository, which qualifies it to be
 * automatically created by component-scanning.
 * @author Anna Merkul.
 */
@Repository
@Transactional
public class CertificateRepositoryImpl implements CertificateRepository {

    private static final String SQL_DELETE = """
            DELETE GiftCertificate WHERE id = :id""";
    private static final String SQL_SELECT_BY_TAG_NAME = """
            SELECT certificates FROM Tag AS tags  JOIN tags.giftCertificates AS certificates WHERE tags.name = :tag_name""";
    private static final String SQL_COUNT_BY_TAG_NAME = """
            SELECT COUNT(certificates.id) FROM Tag AS tags  JOIN tags.giftCertificates AS certificates WHERE tags.name = :tag_name""";
    private static final String SQL_FIND_BY_TAGS_START = """
            SELECT gift_certificate_id, name, description, price, duration, create_date, last_update_date  FROM (
            SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
            JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
            WHERE tag.name='""";
    private static final String SQL_FIND_BY_TAGS_UNION = """
            ' UNION SELECT gift_certificate.*, tag.name AS tag_name FROM gift_certificate
            JOIN tags_gift_certificates ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
            JOIN tag ON tag.tag_id = tags_gift_certificates.tag_id
            WHERE tag.name='""";
    private static final String SQL_FIND_BY_TAGS_END = """
            ') AS used_tags_certificates
            GROUP BY gift_certificate_id
            HAVING COUNT(tag_name)=""";
    private static final String PERCENT = "%";
    private static final int COUNT_CERTIFICATE_RECORDS = 0;
    private static final int RETURN_ROW_VALUE = 1;
    private static final int FIRST_TAG_INDEX = 0;

    private final SessionFactory sessionFactory;
    private final MessageLanguageUtil messageSource;

    @Autowired
    public CertificateRepositoryImpl(MessageLanguageUtil messageSource, SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
        this.messageSource = messageSource;
    }


    @Override
    public int findCountRecords() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(criteriaBuilder.count(root));
        Query<Long> query = session.createQuery(criteriaQuery);
        return  query.getResultList().get(COUNT_CERTIFICATE_RECORDS).intValue();
    }

    @Override
    public int findCountByTagName(String tagName) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(SQL_COUNT_BY_TAG_NAME, Long.class)
                .setParameter("tag_name", tagName).uniqueResult().intValue();
    }

    @Override
    public int findCountByPartNameOrDescription(String part) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = criteriaBuilder.like(certificateRoot.get("name"), "%" + part + "%");
        predicates[1] = criteriaBuilder.like(certificateRoot.get("description"), "%" + part + "%");
        criteriaQuery.select(criteriaBuilder.count(certificateRoot)).where(criteriaBuilder.or(predicates));
        Query<Long> query = session.createQuery(criteriaQuery);
        return query.uniqueResult().intValue();
    }

    @Override
    public int findCountByTagsQuery(List<String> tagsName) {
        String query = buildFindByTagsQuery(tagsName);
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<GiftCertificate> certificateNativeQuery = session.createNativeQuery(query, GiftCertificate.class);
        return certificateNativeQuery.getResultList().size();
    }

    @Override
    public long create(GiftCertificate giftCertificate) {
        Session session = sessionFactory.getCurrentSession();
        session.save(giftCertificate);
        return giftCertificate.getId();
    }

    @Override
    public List<GiftCertificate> show(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public GiftCertificate showById(long id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("id"), id));
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findFirst()
                .orElseThrow(() -> new CustomNotFoundException(messageSource.getMessage("not_found.certificate") + id));
    }

    @Override
    public Optional<GiftCertificate> showByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
        Query<GiftCertificate> query = session.createQuery(criteriaQuery);
        return query.getResultList().stream().findAny();
    }

    @Override
    public void update(GiftCertificate giftCertificate) {
        Session session = sessionFactory.getCurrentSession();
        session.clear();
        session.update(giftCertificate);
    }

    @Override
    public void updateDuration(long certificateId, int duration) {
        Session session = sessionFactory.getCurrentSession();
        GiftCertificate giftCertificate = session.load(GiftCertificate.class, certificateId);
        giftCertificate.setDuration(duration);
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        session.saveOrUpdate(giftCertificate);
    }

    @Override
    public void updatePrice(long certificateId, BigDecimal price) {
        Session session = sessionFactory.getCurrentSession();
        GiftCertificate giftCertificate = session.load(GiftCertificate.class, certificateId);
        giftCertificate.setPrice(price);
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        session.saveOrUpdate(giftCertificate);
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(SQL_DELETE).setParameter("id", id).executeUpdate() == RETURN_ROW_VALUE;
    }

    @Override
    public List<GiftCertificate> showByTagName(int limit, int offset, String name) {
        Query<GiftCertificate> query = sessionFactory.getCurrentSession().createQuery(SQL_SELECT_BY_TAG_NAME, GiftCertificate.class)
                .setParameter("tag_name", name);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> showByPartNameOrDescription(int limit, int offset, String partName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        Predicate[] predicates = new Predicate[2];
        predicates[0] = criteriaBuilder.like(certificateRoot.get("name"), buildPattern(partName));
        predicates[1] = criteriaBuilder.like(certificateRoot.get("description"), buildPattern(partName));
        criteriaQuery.select(certificateRoot).where(criteriaBuilder.or(predicates));
        Query<GiftCertificate> query = session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset);
        return query.getResultList();
    }

    @Override
    public List<GiftCertificate> sortByDateAsc(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(certificateRoot).orderBy(criteriaBuilder.asc(certificateRoot.get("lastUpdateDate")));
        return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }


    @Override
    public List<GiftCertificate> sortByNameAsc(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(certificateRoot).orderBy(criteriaBuilder.asc(certificateRoot.get("name")));
        return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }


    @Override
    public List<GiftCertificate> bothSorting(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(certificateRoot).orderBy(criteriaBuilder.asc(certificateRoot.get("name")),
                criteriaBuilder.asc(certificateRoot.get("createDate")));
        return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }


    @Override
    public List<GiftCertificate> findByTags(int limit, int offset, List<String> tagsName) {
        String query = buildFindByTagsQuery(tagsName);
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<GiftCertificate> certificateQuery = session.createNativeQuery(query, GiftCertificate.class)
                .setMaxResults(limit).setFirstResult(offset);
        return certificateQuery.getResultList();
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

    private String buildPattern(String partName){
        StringBuilder builder = new StringBuilder(PERCENT);
        builder.append(partName).append(PERCENT);
        return builder.toString();
    }
}
