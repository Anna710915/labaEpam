package com.epam.esm.repository.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.util.MessageLanguageUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * The type Tag repository implements methods of the TagRepository
 * interface. The class is annotated with as a repository, which qualifies it to be
 * automatically created by component-scanning.
 * @author Anna Merkul.
 */
@Repository
@Transactional
public class TagRepositoryImpl implements TagRepository {
    private static final String SQL_DELETE_BY_ID = "DELETE Tag WHERE id =: id";
    private static final String SQL_SELECT_WIDELY_USED_USER_TAG_WITH_HIGHEST_ORDERS_COST = """
            SELECT tag.tag_id, tag.name FROM tag
            JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id
            JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
            JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
            JOIN orders ON orders.order_id = orders_gift_certificates.order_id
            JOIN (SELECT users.user_id AS user_id  FROM users
            JOIN orders ON users.user_id = orders.user_id
            GROUP BY users.user_id
            HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost)
            FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders
            JOIN users ON users.user_id = orders.user_id
            GROUP BY users.user_id) AS sum_cost))
            AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id
            GROUP BY max_orders_cost_users.user_id, tag.tag_id
            HAVING SUM(orders_gift_certificates.amount) = (SELECT MAX(max_tag_count) FROM (
            SELECT SUM(orders_gift_certificates.amount) as max_tag_count FROM tag
            JOIN tags_gift_certificates ON tags_gift_certificates.tag_id = tag.tag_id
            JOIN gift_certificate ON gift_certificate.gift_certificate_id = tags_gift_certificates.gift_certificate_id
            JOIN orders_gift_certificates ON orders_gift_certificates.gift_certificate_id = gift_certificate.gift_certificate_id
            JOIN orders ON orders.order_id = orders_gift_certificates.order_id
            JOIN (SELECT users.user_id AS user_id  FROM users
            JOIN orders ON users.user_id = orders.user_id
            GROUP BY users.user_id
            HAVING SUM(order_cost) = (SELECT MAX(sum_user_cost)
            FROM (SELECT SUM(order_cost) as sum_user_cost FROM orders
            JOIN users ON users.user_id = orders.user_id
            GROUP BY users.user_id) AS sum_cost))
            AS max_orders_cost_users ON max_orders_cost_users.user_id = orders.user_id
            GROUP BY max_orders_cost_users.user_id, tag.tag_id) AS tag_counts)""";
    private static final int RETURN_ROW_VALUE = 1;

    private final SessionFactory sessionFactory;
    private final MessageLanguageUtil messageSource;


    @Autowired
    public TagRepositoryImpl(SessionFactory sessionFactory,
                             MessageLanguageUtil messageSource){
        this.sessionFactory = sessionFactory;
        this.messageSource = messageSource;
    }

    @Override
    public long create(Tag tag) {
        Session session = sessionFactory.getCurrentSession();
        session.save(tag);
        return tag.getId();
    }

    @Override
    public int countAllTags() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(criteriaBuilder.count(tagRoot));
        return session.createQuery(criteriaQuery).uniqueResult().intValue();
    }

    @Override
    public List<Tag> show(int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot);
        return session.createQuery(criteriaQuery)
                .setMaxResults(limit).setFirstResult(offset).getResultList();
    }

    @Override
    public Optional<Tag> showByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot).where(criteriaBuilder.equal(tagRoot.get("name"), name));
        return session.createQuery(criteriaQuery).getResultList().stream().findAny();
    }

    @Override
    public Tag showById(long id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot).where(criteriaBuilder.equal(tagRoot.get("id"), id));
        return session.createQuery(criteriaQuery).stream().findFirst()
                .orElseThrow(() -> new CustomNotFoundException(messageSource.getMessage("not_found.tag") + id));
    }

    @Override
    public boolean delete(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery(SQL_DELETE_BY_ID)
                .setParameter("id", id).executeUpdate() == RETURN_ROW_VALUE;
    }

    @Override
    public List<Tag> findWidelyUserTagWithHighestOrdersCost() {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Tag> tagNativeQuery = session
                .createNativeQuery(SQL_SELECT_WIDELY_USED_USER_TAG_WITH_HIGHEST_ORDERS_COST, Tag.class);
        return tagNativeQuery.getResultList();
    }
}
