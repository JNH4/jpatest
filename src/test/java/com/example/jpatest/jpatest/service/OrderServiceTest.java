package com.example.jpatest.jpatest.service;

import com.example.jpatest.jpatest.domain.Address;
import com.example.jpatest.jpatest.domain.Member;
import com.example.jpatest.jpatest.domain.Order;
import com.example.jpatest.jpatest.domain.OrderStatus;
import com.example.jpatest.jpatest.domain.exception.NotEnoughStockException;
import com.example.jpatest.jpatest.domain.item.Book;
import com.example.jpatest.jpatest.domain.item.Item;
import com.example.jpatest.jpatest.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    /**
     * 주문 기능 테스트
     * @throws Exception
     */
    @Test
    public void ItemOrder() throws Exception{
        //given
        Member member = new Member();
        member.setName("member2");
        member.setAddress(new Address("seoul","southCircle" , "123-123"));
        em.persist(member);

        Item book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCount = 2;
        Long orderedId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderedId);
        assertEquals("상품주문시 상태는 ORDER여야 한다.", OrderStatus.ORDER , getOrder.getStatus());
        assertEquals("주문 상품 종류수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void exceedStock (){
        //given
        Member member = new Member();
        member.setName("member2");
        member.setAddress(new Address("seoul","southCircle" , "123-123"));
        em.persist(member);

        Item book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        int orderCount = 12;

        //when
        Long orderedId = orderService.order(member.getId(), book.getId(), orderCount);


        //then
        fail("재고 수량 부족 예외 발생");
    }

    @Test
    public void cancelOrder(){
        //given
        Member member = new Member();
        member.setName("member2");
        member.setAddress(new Address("seoul","southCircle" , "123-123"));
        em.persist(member);

        Item book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        int orderCount = 2;

        //when

        Long orderedId = orderService.order(member.getId(), book.getId(), orderCount);
        orderService.cancelOrder(orderedId);


        //then
        assertEquals(10, book.getStockQuantity());
    }
}