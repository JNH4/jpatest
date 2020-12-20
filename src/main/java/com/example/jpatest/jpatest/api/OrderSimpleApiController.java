package com.example.jpatest.jpatest.api;

import com.example.jpatest.jpatest.domain.Address;
import com.example.jpatest.jpatest.domain.Order;
import com.example.jpatest.jpatest.domain.OrderStatus;
import com.example.jpatest.jpatest.repository.OrderRepository;
import com.example.jpatest.jpatest.repository.OrderSearch;
import com.example.jpatest.jpatest.repository.order.simplequery.OrderSimpleQueryRepository;
import com.example.jpatest.jpatest.repository.order.simplequery.SimpleOrderQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  n To One( Many To One, One To One )
 *  Order
 *  Order ->  Member
 *  Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1 (){
        List <Order> orders = orderRepository.findAllByString(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return orders;
    }

    /**
     * Entity를 직접 노출하지 않고 DTO를 사용하여용 조회
     * N + 1 문제 발생 (지연로딩은 영속성 컨텍스트에서 조회 -> 없으면 쿼리 나감)
     * 쿼리가 최대 총 1 + K*N 번 나감
     * 해결법으로 Fetch join 사용
     *
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List <Order> orders = orderRepository.findAllByString(new OrderSearch());
        List <SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 페치 조인으로 한번에 조인 후 컬럼들을 긁어옴
     * 긁어온 컬럼들은 jpa가 알아서 슬라이싱해서 넣어줌
     *
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        List <SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 성능은 v3보다 조금 더 나음(거의 안남..)
     * DTO 로 조회하여  확장성 및 범용성이 없음
     * jpql에서 조회시 dto의 풀패키지경로를 적어줘야되서 구조가 복잡할 경우 복잡하고 헷갈릴수있
     * @return
     */
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> ordersV4(){

        List <SimpleOrderQueryDto> result = orderSimpleQueryRepository.findOrderDtos();
        return result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}

