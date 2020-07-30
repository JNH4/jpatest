package com.example.jpatest.jpatest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private  Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //LocalDateTime 타입으로 쓰면 자동으로 연동됨
    //Date는 다른 어노테이션 붙여줘야함
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //연관관계 메서드//

    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /**
     * create Order
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem ... orderItems){

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    /**
     * Order cancel
     */
    public void cancelOrder() {
        if(delivery.getStatus() == DeliveryStatus.COMP) throw new IllegalStateException("Already delivered");
        this.setStatus(OrderStatus.CANCELED);

        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }

    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }
}
