package com.example.nikfrvSpring.controller;


import com.example.nikfrvSpring.payload.request.UserRequest;
import com.example.nikfrvSpring.payload.response.UserResponse;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserRepository userRepository;

    @PutMapping("/promote")
    public void promoteUser (@PathVariable Long userId){
        userService.promoteToAdmin(userId);
    }

    @GetMapping("/getAllUsers")
    public List<UserResponse> findAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.removeUser(id);
    }
}
