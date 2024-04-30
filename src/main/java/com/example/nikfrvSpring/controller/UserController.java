package com.example.nikfrvSpring.controller;

import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.UserAlreadyExistsException;
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
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) throws UserAlreadyExistsException {
        try {
            userService.saveUser(username, password);
            return ResponseEntity.ok("User successfully registered!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error occured");
        }

    }

    @PostMapping ("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password){
        boolean isAuthenticated = userService.authenticateUser(username, password);

        if (isAuthenticated){
            String message = "User successfully authorized";
            return ResponseEntity.ok().body(message);
        }else {
            String errorMessage = "Wrong username or password";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

    @GetMapping("/getAllUsers")
    public List<User> findAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteuser(@PathVariable Long id){
        try {
            return ResponseEntity.ok(userService.delete(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error occured");
        }
    }
}
