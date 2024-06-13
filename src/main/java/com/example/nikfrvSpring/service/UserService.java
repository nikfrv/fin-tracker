package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.UserAlreadyExistsException;
import com.example.nikfrvSpring.payload.request.UserRequest;
import com.example.nikfrvSpring.payload.response.UserResponse;
import com.example.nikfrvSpring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    private UserResponse mapToResponse(User user){
        return new UserResponse(user.getUsername(), user.getPassword());
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }



    public void saveUser(UserRequest userRequest) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(userRequest.username()) != null){
            throw new UserAlreadyExistsException("User already exists ");
        }
        User user = new User();
        user.setUsername(userRequest.username());
        user.setPassword(userRequest.password());
        userRepository.save(user);
    }

    public boolean authenticateUser(UserRequest userRequest){
        User user = userRepository.findByUsername(userRequest.username());

        if (user != null && user.getPassword().equals(userRequest.password())){
            return true;
        }

        return false;
    }

    public Long removeUser (Long id){
        userRepository.deleteById(id);
        return id;
    }


}
