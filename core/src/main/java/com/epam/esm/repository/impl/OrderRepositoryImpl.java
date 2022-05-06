package com.epam.esm.repository.impl;

import com.epam.esm.exception.CustomNotFoundException;
import com.epam.esm.model.entity.Order;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.util.MessageLanguageUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Transactional
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final SessionFactory sessionFactory;
    private final MessageLanguageUtil messageSource;

    @Autowired
    public OrderRepositoryImpl(SessionFactory sessionFactory,
                               MessageLanguageUtil messageSource){
        this.sessionFactory = sessionFactory;
        this.messageSource = messageSource;
    }

    @Override
    public long createOrder(Order order) {
        Session session = sessionFactory.getCurrentSession();
        session.save(order);
        return order.getOrderId();
    }

    @Override
    public List<Order> selectPaginatedUserOrders(long userId, int limit, int offset) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get("user"), userId));
        return session.createQuery(criteriaQuery).setMaxResults(limit).setFirstResult(offset).getResultList();
    }


    @Override
    public Order findUserOrder(long orderId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get("orderId"), orderId));
        return session.createQuery(criteriaQuery).getResultList().stream().findFirst()
                .orElseThrow(() -> new CustomNotFoundException(messageSource.getMessage("not_found.order") + orderId));
    }

    @Override
    public int findCountAllRecords(long userId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        criteriaQuery.select(criteriaBuilder.count(orderRoot)).where(criteriaBuilder.equal(orderRoot.get("user"), userId));
        return session.createQuery(criteriaQuery).uniqueResult().intValue();
    }
}
