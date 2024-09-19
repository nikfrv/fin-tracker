package com.example.nikfrvSpring.controller;

import com.example.nikfrvSpring.payload.request.UserRequest;
import com.example.nikfrvSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRequest userRequest) {
        userService.saveUser(userRequest);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody UserRequest userRequest) {
        String token = userService.authenticateUser(userRequest);
        if (token != null) {
            return token;
        } else {
            throw new RuntimeException("error");  // выбрасываем исключение или возвращаем сообщение об ошибке
        }
    }

}
