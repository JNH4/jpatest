package com.example.jpatest.jpatest;

import com.example.jpatest.jpatest.domain.*;
import com.example.jpatest.jpatest.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;

        public void dbInit1(){

            Member member = new Member();
            member.setName("UserA");
            member.setAddress(new Address("Seoul","Guro","12354"));

            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA1 BOOK");
            book1.setPrice(10000);
            book1.setStockQuantity(100);

            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA2 BOOK");
            book2.setPrice(13000);
            book2.setStockQuantity(100);

            em.persist(book2);

            OrderItem orderItem1 =  OrderItem.createOrderItem(book1,10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 13000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2(){

            Member member = new Member();
            member.setName("UserB");
            member.setAddress(new Address("Seoul","Dongdaemoon","62532"));

            em.persist(member);

            Book book1 = new Book();
            book1.setName("Spring1 BOOK");
            book1.setPrice(20000);
            book1.setStockQuantity(100);

            em.persist(book1);

            Book book2 = new Book();
            book2.setName("Spring2 BOOK");
            book2.setPrice(33000);
            book2.setStockQuantity(100);

            em.persist(book2);

            OrderItem orderItem1 =  OrderItem.createOrderItem(book1,20000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 33000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
    }
}

