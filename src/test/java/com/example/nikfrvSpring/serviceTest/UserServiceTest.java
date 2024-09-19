/*package com.example.nikfrvSpring.serviceTest;

import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.UserAlreadyExistsException;
import com.example.nikfrvSpring.payload.request.UserRequest;
import com.example.nikfrvSpring.payload.response.UserResponse;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers(){
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        UserResponse userResponse = new UserResponse("testUser", "testPass");
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponse> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(userResponse.username(), users.get(0).username());
        assertEquals(userResponse.password(), users.get(0).password());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    void testSaveUser_Success() throws UserAlreadyExistsException {
        UserRequest userRequest = new UserRequest("newUser", "newPass");
        when(userRepository.findByUsername("newUser")).thenReturn(null);

        userService.saveUser(userRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSaveUser_UserAlreadyExists() {
        UserRequest userRequest = new UserRequest("existingUser", "pass");
        when(userRepository.findByUsername("existingUser")).thenReturn(new User());

        assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(userRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAuthenticateUser_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        UserRequest userRequest = new UserRequest("testUser", "testPass");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        boolean isAuthenticated = userService.authenticateUser(userRequest);

        assertTrue(isAuthenticated);
    }

    @Test
    void testAuthenticateUser_Failure() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        UserRequest userRequest = new UserRequest("testUser", "wrongPass");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        boolean isAuthenticated = userService.authenticateUser(userRequest);

        assertFalse(isAuthenticated);
    }


    @Test
    void testRemoveUser() {
        Long userId = 1L;

        Long removedId = userService.removeUser(userId);

        assertEquals(userId, removedId);
        verify(userRepository, times(1)).deleteById(userId);
    }
}
