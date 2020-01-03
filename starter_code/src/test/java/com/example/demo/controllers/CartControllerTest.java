package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
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

import static org.mockito.Mockito.mock;

public class CartControllerTest {

    private static final Logger log = LoggerFactory.getLogger(CartControllerTest.class);
    private ItemController itemController;
    private UserController userController;
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private ItemRepository itempRepo = mock(ItemRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        itemController = new ItemController();
        userController = new UserController();
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itempRepo);
//        TestUtils.injectObjects(cartController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void addItemToCart(){
        User r = new User();
        r.setUsername("test");
        r.setPassword("test1");
        Cart cart = new Cart();
        cart.setUser(r);
        r.setCart(cart);
//        final ResponseEntity<User> response = userController.createUser(r);
//        User user = response.getBody();
//        assertEquals(200, response.getStatusCodeValue());
        Mockito.when(userRepo.findByUsername(r.getUsername())).thenReturn(r);

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It is a fruit");
        Mockito.when(itempRepo.findById(item.getId())).thenReturn(java.util.Optional.of(item));
        
        ModifyCartRequest cartReq = new ModifyCartRequest();
        cartReq.setUsername("test");
        cartReq.setQuantity(1);
        cartReq.setItemId(1L);
//        Mockito.when(userRepo.findByUsername(r.getUsername())).thenReturn(user);
        ResponseEntity<Cart> output = cartController.addTocart(cartReq);
        assertEquals(200, output.getStatusCodeValue());
        Cart cartObj = output.getBody();
        assertEquals("test", cartObj.getUser().getUsername());
        assertEquals("Apple", cartObj.getItems().get(0).getName());
    }

    @Test
    public void removeFromCart() {

        User r = new User();
        r.setUsername("test");
        r.setPassword("test1");
        Cart cart = new Cart();
        cart.setUser(r);
        r.setCart(cart);
        Mockito.when(userRepo.findByUsername(r.getUsername())).thenReturn(r);

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It's a fruit");
        Mockito.when(itempRepo.findById(item.getId())).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest cartReq = new ModifyCartRequest();
        cartReq.setUsername("test");
        cartReq.setQuantity(1);
        cartReq.setItemId(1L);

        ResponseEntity<Cart> output = cartController.removeFromcart(cartReq);
        assertEquals(200, output.getStatusCodeValue());
        Cart cartObj = output.getBody();
        assertEquals("test", cartObj.getUser().getUsername());
        assertEquals(0, cartObj.getItems().size());
    }

}
