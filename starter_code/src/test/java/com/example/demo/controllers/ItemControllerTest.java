package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ItemControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemById() {

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It's a fruit");

        Mockito.when(itemRepo.findById(1L)).thenReturn(java.util.Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Apple", response.getBody().getName());
        assertEquals("It's a fruit", response.getBody().getDescription());
    }

    @Test
    public void getItemByName() {

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It is a fruit");

        Item item1 = new Item();
        item1.setId(2L);
        item1.setName("Apple");
        item1.setPrice(bg);
        item1.setDescription("It is a fruit");

        List<Item> addItems = new ArrayList<>();
        addItems.add(item);
        addItems.add(item1);

        Mockito.when(itemRepo.findByName(item.getName())).thenReturn(addItems);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(item.getName());
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertEquals("Apple", items.get(0).getName());
        assertEquals("Apple", items.get(1).getName());
    }

    @Test
    public void getAllItems()
    {
        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It is a fruit");

        Item item1 = new Item();
        item1.setId(2L);
        item1.setName("Tomato");
        item1.setPrice(bg);
        item1.setDescription("It is a vegitable");

        List<Item> addItems = new ArrayList<>();
        addItems.add(item);
        addItems.add(item1);

        Mockito.when(itemRepo.findAll()).thenReturn(addItems);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertEquals("Apple", items.get(0).getName());
        assertEquals("Tomato", items.get(1).getName());
    }

}
