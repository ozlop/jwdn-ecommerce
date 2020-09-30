package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    private CartController cartController;

    private CartRepository cartRepository = mock(CartRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_add_to_cart() {
        ModifyCartRequest mockModifyCartRequest = new ModifyCartRequest();
        mockModifyCartRequest.setUsername("test");
        mockModifyCartRequest.setItemId(1L);
        mockModifyCartRequest.setQuantity(1);

        User mockUser = new User();
        Cart mockCart = new Cart();
        Item mockItem = new Item();

        mockUser.setId(1);
        mockUser.setUsername("test");
        mockUser.setPassword("password");

        mockItem.setId(1L);
        mockItem.setName("Round Widget");
        mockItem.setDescription("A widget that is round");
        mockItem.setPrice(BigDecimal.valueOf(2.99));

        mockUser.setCart(mockCart);

        when(userRepository.findByUsername("test")).thenReturn(mockUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.addTocart(mockModifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(2.99), cart.getTotal());

    }

    @Test
    public void add_to_cart_user_does_not_exist() {
        ModifyCartRequest mockModifyCartRequest = new ModifyCartRequest();
        mockModifyCartRequest.setUsername("test");
        mockModifyCartRequest.setItemId(1L);
        mockModifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(mockModifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNull(cart);

    }

    @Test
    public void add_to_cart_item_does_not_exist() {
        ModifyCartRequest mockModifyCartRequest = new ModifyCartRequest();
        mockModifyCartRequest.setUsername("test");
        mockModifyCartRequest.setItemId(1L);
        mockModifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername("test")).thenReturn(new User());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(mockModifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNull(cart);

    }

    @Test
    public void verify_remove_from_cart() {
        ModifyCartRequest mockModifyCartRequest = new ModifyCartRequest();
        mockModifyCartRequest.setUsername("test");
        mockModifyCartRequest.setItemId(1L);
        mockModifyCartRequest.setQuantity(1);

        User mockUser = new User();
        Cart mockCart = new Cart();
        Item mockItem = new Item();

        mockUser.setId(1);
        mockUser.setUsername("test");
        mockUser.setPassword("password");

        mockItem.setId(1L);
        mockItem.setName("Round Widget");
        mockItem.setDescription("A widget that is round");
        mockItem.setPrice(BigDecimal.valueOf(2.99));

        mockUser.setCart(mockCart);

        when(userRepository.findByUsername("test")).thenReturn(mockUser);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(mockItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.removeFromcart(mockModifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(BigDecimal.valueOf(-2.99), cart.getTotal());

    }

    @Test
    public void remove_from_cart_user_does_not_exist() {
        ModifyCartRequest mockModifyCartRequest = new ModifyCartRequest();
        mockModifyCartRequest.setUsername("test");
        mockModifyCartRequest.setItemId(1L);
        mockModifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.removeFromcart(mockModifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNull(cart);

    }

    @Test
    public void remove_from_cart_item_does_not_exist() {
        ModifyCartRequest mockModifyCartRequest = new ModifyCartRequest();
        mockModifyCartRequest.setUsername("test");
        mockModifyCartRequest.setItemId(1L);
        mockModifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername("test")).thenReturn(new User());
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromcart(mockModifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNull(cart);

    }

}
