package com.example.jpatest.jpatest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    // 연관관계의 주인이 아닌 order의 member를 보여주는 거울.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
