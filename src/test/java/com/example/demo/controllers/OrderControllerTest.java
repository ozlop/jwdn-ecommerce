package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);



    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
        TestUtils.injectObject(orderController, "userRepository", userRepository);
    }

    @Test
    public void verify_order_submit() {
        User mockUser = new User();
        Cart mockCart = new Cart();
        Item mockItem = new Item();

        mockUser.setId(1);
        mockUser.setUsername("test");
        mockUser.setPassword("password");

        mockCart.setUser(mockUser);
        mockCart.setItems(Collections.singletonList(mockItem));
        mockCart.setTotal(BigDecimal.valueOf(1.00));

        mockUser.setCart(mockCart);

        when(userRepository.findByUsername("test")).thenReturn(mockUser);


        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(mockUser, userOrder.getUser());

    }

    @Test
    public void order_submit_user_not_found() {
        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertEquals(404, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertNull(userOrder);

    }

    @Test
    public void verify_get_orders_for_user() {
        User mockUser = new User();
        Item mockItem = new Item();
        UserOrder mockUserOrder = new UserOrder();

        mockUser.setId(1);
        mockUser.setUsername("test");
        mockUser.setPassword("password");

        mockItem.setId(1L);
        mockItem.setName("test item");
        mockItem.setPrice(BigDecimal.valueOf(1.00));

        mockUserOrder.setId(1L);
        mockUserOrder.setUser(mockUser);
        mockUserOrder.setTotal(BigDecimal.valueOf(1.00));
        mockUserOrder.setItems(Collections.singletonList(mockItem));

        when(userRepository.findByUsername("test")).thenReturn(mockUser);
        when(orderRepository.findByUser(mockUser)).thenReturn(Collections.singletonList(mockUserOrder));


        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrderList = response.getBody();

        assertNotNull(userOrderList);
        assertEquals(mockUser, userOrderList.get(0).getUser());

    }

    @Test
    public void get_orders_for_user_does_exist() {
        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertEquals(404, response.getStatusCodeValue());

        List<UserOrder> userOrder = response.getBody();

        assertNull(userOrder);

    }

}
