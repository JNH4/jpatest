package com.example.jpatest.jpatest.service;

import com.example.jpatest.jpatest.domain.item.Item;
import com.example.jpatest.jpatest.domain.item.Movie;
import com.example.jpatest.jpatest.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void saveItem() {
        //given
        Item item = (Item)new Movie();
        item.setName("Bando");

        //when
        Long savedItem = itemService.saveItem(item);

        //then
        assertEquals(item, itemRepository.findOne(savedItem));

    }

    @Test
    public void findItems(){
    }

    @Test
    public void findOne() {
    }
}