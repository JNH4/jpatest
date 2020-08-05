package com.example.jpatest.jpatest.service;

import com.example.jpatest.jpatest.domain.Delivery;
import com.example.jpatest.jpatest.domain.Member;
import com.example.jpatest.jpatest.domain.Order;
import com.example.jpatest.jpatest.domain.OrderItem;
import com.example.jpatest.jpatest.domain.item.Item;
import com.example.jpatest.jpatest.repository.ItemRepository;
import com.example.jpatest.jpatest.repository.MemberRepository;
import com.example.jpatest.jpatest.repository.OrderRepository;
import com.example.jpatest.jpatest.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private  final ItemRepository itemRepository;
    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        //new 키워드로 생성자 이용하여 객체 생성할수 있는데 다른 사람들이 사용하지 않길 바란다면
        //생성자에 protected 키워드 걸어
        Order newOrder = Order.createOrder(member, delivery, orderItem);


        /*
         *  주문 생성
         *  cascade 때문에 OrderItem과 Delivery가 자동업데이트 됨
         *  cascade는 유일 참조 관계일때만 사용하는게 좋음
         *  그게 아니라면 deliveryRepository, OrderItemRepository 따로만들어서 저장해줘야함
         */
        orderRepository.save(newOrder);

        return newOrder.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        //JPA의 장점
        //서비스 단에서 비즈니스 로직을 건들지 않아도
        //변경내역 감지에 의해 엔티티에서 바뀌어진 속성들을 자동으로 쿼리날려줌.(도메인 모델 패턴)
        //서비스 단에서 쿼리를 통해 비즈니스 로직을 전개하는 방식을 트랜잭션 스크립트 패턴이라고 한다.
        //둘 중 상황에 맞는 패턴을 선택하여 개발하면 됨.
        order.cancelOrder();
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
