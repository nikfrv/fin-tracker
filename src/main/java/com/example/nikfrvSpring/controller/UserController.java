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
@RequestMapping("/api")
public class UserController {
    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserRepository userRepository;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRequest userRequest) {
        userService.saveUser(userRequest);
    }

    @PostMapping ("/login")
    public void loginUser(@RequestBody UserRequest userRequest){
        boolean isAuthenticated = userService.authenticateUser(userRequest );
        if (isAuthenticated){
            String message = "User successfully authorized";
            ResponseEntity.ok().body(message);
        }else {
            String errorMessage = "Wrong username or password";
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
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
