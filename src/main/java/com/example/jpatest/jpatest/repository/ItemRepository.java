package com.example.jpatest.jpatest.repository;

import com.example.jpatest.jpatest.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long save(Item item){
        //Id가 PK이기 때문에 이게 null이면 아직 저장되지 않았다는 뜻
        if(item.getId() == null) em.persist(item);
        else em.merge(item);

        return item.getId();
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
