package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.UserAlreadyExistsException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.UserRequest;
import com.example.nikfrvSpring.payload.response.UserResponse;
import com.example.nikfrvSpring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers(){
        List<User> users = userRepository.findAll();

        return users.stream().
                map(user -> new UserResponse(user.getUsername(),
                user.getPassword()))
                .collect(Collectors.toList());
    }

    public void saveUser(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.username()).isPresent()){
            throw new UserAlreadyExistsException("User already exists ");
        }
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());
        userRepository.save(user);
    }

    public boolean authenticateUser(UserRequest userRequest){
        User user = userRepository.findByUsername(userRequest.username()).orElseThrow(
                ()-> new UserNotFoundException("User not found"));
        if (user.getPassword().equals(userRequest.password())){
            return true;
        }
        return false;
    }

    public void removeUser(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id " + id +" not found"));
        userRepository.delete(user);
    }


}
