package com.ecommerce.controllers;

import com.ecommerce.TestUtil;
import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.repositories.CartRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import com.ecommerce.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepo=mock(UserRepository.class);
    private CartRepository cartRepo=mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder= mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() throws  Exception{
        userController= new UserController();
        TestUtil.injectObjects(userController, "userRepository", userRepo);
        TestUtil.injectObjects(userController, "cartRepository", cartRepo);
        TestUtil.injectObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
    }

    @Test
    public void TestCreateUserValid() throws Exception{
        when(passwordEncoder.encode("P@ssword1234")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("P@ssword1234");
        createUserRequest.setConfirmPassword("P@ssword1234");

        final ResponseEntity<User> response= userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());

        User user= response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("hashedPassword", user.getPassword());
    }
}
