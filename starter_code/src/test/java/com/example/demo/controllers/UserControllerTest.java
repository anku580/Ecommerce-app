package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.mock;

public class UserControllerTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerTest.class);
    private UserController userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws Exception{
        Mockito.when(encoder.encode("test1")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test123456");
        r.setConfirmPassword("test123456");

        final ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();

        assertNotNull(response);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void getUserInformation() throws Exception{

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("test123456");
        r.setConfirmPassword("test123456");

        final ResponseEntity<User> response = userController.createUser(r);
        User user = response.getBody();

//        Mockito.when(userRepo.save(user)).thenReturn(user);
        Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        final ResponseEntity<User> response1 = userController.findByUserName(user.getUsername());

        assertNotNull(response);
        assertNotNull(response1);
        assertEquals(200, response.getStatusCodeValue());
        log.info(String.valueOf(response1));
        assertEquals(200, response1.getStatusCodeValue());

        User u = response1.getBody();
        assertEquals("test", u.getUsername());
    }

    @Test
    public void getUserInformationById() {

        CreateUserRequest cuser = new CreateUserRequest();
        cuser.setUsername("dummy");
        cuser.setPassword("test123456");
        cuser.setConfirmPassword("test123456");

        ResponseEntity<User> response =  userController.createUser(cuser);
        User user = response.getBody();
        Mockito.when(userRepo.findById(user.getId())).thenReturn(java.util.Optional.ofNullable(user));

        ResponseEntity<User> response1 = userController.findById(user.getId());
        assertEquals(200, response1.getStatusCodeValue());
        assertEquals("dummy", response1.getBody().getUsername());
    }
}
