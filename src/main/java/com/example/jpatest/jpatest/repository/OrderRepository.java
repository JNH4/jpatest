package com.example.jpatest.jpatest.repository;

import com.example.jpatest.jpatest.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private  final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    //JPA 동적 쿼리 전략
    public List<Order> findAll (OrderSearch orderSearch){
        String jpql = "select o from Order o join o.member m"
                + "where o.status = : status"
                + "and m.name like :name";

        return em.createQuery(jpql , Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(100)
                .getResultList();

    }

    /**
     * JPA Criteria
     * not Used
     *
     * 복잡한 쿼리 사용에는 적합하지 않기떄문에 QueryDsl 사용     * @param orderSearch
     * @return
     */
    public List<Order> findAllByCriteria (OrderSearch orderSearch){

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery <Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        // ...
        return null;
    }
/*
    public List<Order> findAllByString(OrderSearch orderSearch){

    }
*/
}
