package com.ecommerce.controllers;

import com.ecommerce.TestUtil;
import com.ecommerce.model.persistence.Cart;
import com.ecommerce.model.persistence.Item;
import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.UserOrder;
import com.ecommerce.model.persistence.repositories.CartRepository;
import com.ecommerce.model.persistence.repositories.OrderRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import com.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo=mock(UserRepository.class);
    private OrderRepository orderRepo=mock(OrderRepository.class);

    @Before
    public void setUp() throws  Exception{
        orderController= new OrderController();
        TestUtil.injectObjects(orderController, "userRepository", userRepo);
        TestUtil.injectObjects(orderController, "orderRepository", orderRepo);
    }

    @Test
    public void TestSubmitOrder() throws Exception{
        Item item=new Item();
        item.setId(1l);
        item.setName("MacBook");
        item.setDescription("13 Gray");
        item.setPrice(BigDecimal.valueOf(1200.13));

        Item item2=new Item();
        item2.setId(2l);
        item2.setName("MacBook");
        item2.setDescription("13 Black");
        item2.setPrice(BigDecimal.valueOf(1110.03));

        List<Item> listOfItems= new ArrayList<>();
        listOfItems.add(item);
        listOfItems.add(item2);

        Cart cart= new Cart();
        cart.setId(1l);
        cart.setItems(listOfItems);
        cart.setTotal(BigDecimal.valueOf(11230.03));

         User user= new User();
         user.setUsername("abc");
         user.setPassword("abcdefg");
         user.setCart(cart);

        doReturn(user).when(userRepo).findByUsername(user.getUsername());
        UserOrder order = UserOrder.createFromCart(user.getCart());
        doReturn(Optional.of(order)).when(orderRepo).save(order);

        final ResponseEntity<UserOrder> response= orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        UserOrder responseBody= response.getBody();
        assertEquals(2,responseBody.getItems().size());
        assertEquals(BigDecimal.valueOf(11230.03), responseBody.getTotal());
    }


    @Test
    public void TestGetOrdersForUser(){
        Item item=new Item();
        item.setId(1l);
        item.setName("MacBook");
        item.setDescription("13 Gray");
        item.setPrice(BigDecimal.valueOf(1200.13));

        Item item2=new Item();
        item2.setId(2l);
        item2.setName("MacBook");
        item2.setDescription("13 Black");
        item2.setPrice(BigDecimal.valueOf(1110.03));

        List<Item> listOfItems= new ArrayList<>();
        listOfItems.add(item);
        listOfItems.add(item2);

        Cart cart= new Cart();
        cart.setId(1l);
        cart.setItems(listOfItems);
        cart.setTotal(BigDecimal.valueOf(11230.03));

        User user= new User();
        user.setUsername("abc");
        user.setPassword("abcdefg");
        user.setCart(cart);

        doReturn(user).when(userRepo).findByUsername(user.getUsername());
        UserOrder order = UserOrder.createFromCart(user.getCart());
        List<UserOrder> list= new ArrayList<>();
        list.add(order);
        order.setId(2l);
        list.add(order);
        doReturn(list).when(orderRepo).findByUser(user);

        final ResponseEntity<List<UserOrder>> response= orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        List<UserOrder> responseBody= response.getBody();
        assertEquals(2,responseBody.size());

    }


    @Test
    public void TestGetOrdersForInvalidUser(){
        Item item=new Item();
        item.setId(1l);
        item.setName("MacBook");
        item.setDescription("13 Gray");
        item.setPrice(BigDecimal.valueOf(1200.13));

        Item item2=new Item();
        item2.setId(2l);
        item2.setName("MacBook");
        item2.setDescription("13 Black");
        item2.setPrice(BigDecimal.valueOf(1110.03));

        List<Item> listOfItems= new ArrayList<>();
        listOfItems.add(item);
        listOfItems.add(item2);

        Cart cart= new Cart();
        cart.setId(1l);
        cart.setItems(listOfItems);
        cart.setTotal(BigDecimal.valueOf(11230.03));

        User user= new User();
        user.setUsername("abc");
        user.setPassword("abcdefg");
        user.setCart(cart);

        doReturn(user).when(userRepo).findByUsername("xyz");
        UserOrder order = UserOrder.createFromCart(user.getCart());
        List<UserOrder> list= new ArrayList<>();
        list.add(order);
        order.setId(2l);
        list.add(order);
        doReturn(list).when(orderRepo).findByUser(user);

        final ResponseEntity<List<UserOrder>> response= orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(404, response.getStatusCode().value());

    }



}
