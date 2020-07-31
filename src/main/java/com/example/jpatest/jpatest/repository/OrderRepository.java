package com.example.jpatest.jpatest.repository;

import com.example.jpatest.jpatest.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
    /*
    public Order findByName(){
        return em.createQuery("").setParameter().getResultList();

    }*/
}
