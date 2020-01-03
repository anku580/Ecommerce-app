package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
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

public class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private ItemRepository itempRepo = mock(ItemRepository.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void submitOrder() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test1");
        Cart cart = new Cart();
        user.setCart(cart);

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It's a fruit");
        List<Item> addItems = new ArrayList<>();
        addItems.add(item);
        cart.setItems(addItems);
        Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertEquals(200, response.getStatusCodeValue());
        UserOrder output = response.getBody();
        assertEquals("It is a fruit", output.getItems().get(0).getDescription());
    }

    @Test
    public void getOrdersForUserTest() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test1");
        Cart cart = new Cart();
        user.setCart(cart);
        Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It is a fruit");
        List<Item> addItems = new ArrayList<>();
        addItems.add(item);
        cart.setItems(addItems);

        UserOrder order = new UserOrder();
        order.setItems(addItems);
        order.setUser(user);
        order.setTotal(bg);

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);

        Mockito.when(orderRepo.findByUser(user)).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orderHistory = response.getBody();
        assertEquals("Apple", orderHistory.get(0).getItems().get(0).getName());
    }

}
