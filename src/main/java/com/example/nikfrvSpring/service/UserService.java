package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.UserAlreadyExistsException;
import com.example.nikfrvSpring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public void saveUser(String username, String password) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username) != null){
            throw new UserAlreadyExistsException("User already exists ");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    }

    public boolean authenticateUser(String username, String password){
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)){
            return true;
        }

        return false;
    }

    public Long delete (Long id){
        userRepository.deleteById(id);
        return id;
    }


}
