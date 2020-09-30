package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository);
        TestUtils.injectObject(userController, "cartRepository", cartRepository);
        TestUtils.injectObject(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void verify_create_user() {
        when(encoder.encode("password")).thenReturn("thisIsHashed");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("password");
        request.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());

    }

    @Test
    public void verify_create_user_password_validation() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("test");
        request.setPassword("short");
        request.setConfirmPassword("short");

        final ResponseEntity<User> response = userController.createUser(request);
        User user = response.getBody();

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        assertNull(user);

    }

    @Test
    public void verify_find_user_by_id() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("test");
        mockUser.setPassword("password");
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(mockUser.getId(), user.getId());

    }

    @Test
    public void find_user_by_id_does_not_exist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(404, response.getStatusCodeValue());

        User user = response.getBody();

        assertNull(user);

    }

    @Test
    public void verify_find_user_by_username() {
        User mockUser = new User();
        mockUser.setUsername("test");
        when(userRepository.findByUsername("test")).thenReturn(mockUser);

        ResponseEntity<User> response = userController.findByUserName("test");
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(mockUser.getUsername(), "test");


    }

    @Test
    public void find_user_by_username_does_not_exist() {
        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName("test");
        assertEquals(404, response.getStatusCodeValue());

        User user = response.getBody();

        assertNull(user);

    }
}
