package com.example.jpatest.jpatest.repository;

import com.example.jpatest.jpatest.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;

}
