package com.ecommerce.controllers;

import com.ecommerce.TestUtil;
import com.ecommerce.model.persistence.User;
import com.ecommerce.model.persistence.repositories.CartRepository;
import com.ecommerce.model.persistence.repositories.UserRepository;
import com.ecommerce.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.configuration.IMockitoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    public void TestCreateUserInValidPassword() throws Exception{
        when(passwordEncoder.encode("P@sswo")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("P@sswo");
        createUserRequest.setConfirmPassword("P@sswo");

        final ResponseEntity<User> response= userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void TestCreateUserNotMatchPassword() throws Exception{
        when(passwordEncoder.encode("P@ssword1234")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("P@ssword1234");
        createUserRequest.setConfirmPassword("p@ssword1234");

        final ResponseEntity<User> response= userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
    }




    @Test
    public void TestFindByUserNameValid(){
        when(passwordEncoder.encode("P@ssword1234")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("useruser");
        createUserRequest.setPassword("P@ssword1234");
        createUserRequest.setConfirmPassword("P@ssword1234");
        userController.createUser(createUserRequest);
        User user= new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword("hashedPassword");

        doReturn(user).when(userRepo).findByUsername(createUserRequest.getUsername());
        final ResponseEntity<User> response= userController.findByUserName(user.getUsername());

        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        User responseBody= response.getBody();
        Assert.assertEquals("useruser", responseBody.getUsername());
    }

    @Test
    public void TestFindByUserNameInValid(){
        when(passwordEncoder.encode("P@ssword1234")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("useruser");
        createUserRequest.setPassword("P@ssword1234");
        createUserRequest.setConfirmPassword("P@ssword1234");
        userController.createUser(createUserRequest);
        User user= new User();
        user.setUsername(createUserRequest.getUsername());
        user.setPassword("hashedPassword");

        doReturn(user).when(userRepo).findByUsername(createUserRequest.getUsername());
        final ResponseEntity<User> response= userController.findByUserName(user.getUsername()+"abc");

        assertEquals(404,response.getStatusCode().value());

    }


    @Test
    public void TestFindById(){
        when(passwordEncoder.encode("P@ssword1234")).thenReturn("hashedPassword");
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("useruser");
        createUserRequest.setPassword("P@ssword1234");
        createUserRequest.setConfirmPassword("P@ssword1234");
        userController.createUser(createUserRequest);
        User user= new User();
        user.setId(5l);
        user.setUsername(createUserRequest.getUsername());
        user.setPassword("hashedPassword");
        doReturn(Optional.of(user)).when(userRepo).findById(user.getId());
        final ResponseEntity<User> response= userController.findById(user.getId());

        assertNotNull(response);
        assertEquals(200,response.getStatusCode().value());
        User responseBody= response.getBody();
        Assert.assertEquals(5l, responseBody.getId());
    }



}
