package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_get_items() {
        Item mockItem = new Item();
        List<Item> mockItemsList = Collections.singletonList(mockItem);

        when(itemRepository.findAll()).thenReturn(mockItemsList);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals(mockItem, itemList.get(0));

    }

    @Test
    public void verify_get_item_by_id() {
        Item mockItem = new Item();
        mockItem.setId(1L);
        mockItem.setName("test");
        mockItem.setDescription("password");
        mockItem.setPrice(BigDecimal.valueOf(1.00));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();

        assertNotNull(item);
        assertEquals(mockItem.getId(), item.getId());
        assertEquals(mockItem.getName(), item.getName());
        assertEquals(mockItem.getDescription(), item.getDescription());
        assertEquals(mockItem.getPrice(), item.getPrice());

    }

    @Test
    public void item_by_id_does_not_exist() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(404, response.getStatusCodeValue());

        Item item = response.getBody();

        assertNull(item);
    }

    @Test
    public void verify_get_items_by_name() {
        Item mockItem = new Item();
        List<Item> mockItemsList = Collections.singletonList(mockItem);

        when(itemRepository.findByName("test")).thenReturn(mockItemsList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");

        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals(mockItem, itemList.get(0));

    }

    @Test
    public void items_by_name_no_match() {
        when(itemRepository.findByName("test")).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        assertEquals(404, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNull(itemList);
    }

}
