package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Role;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.UserAlreadyExistsException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.UserRequest;
import com.example.nikfrvSpring.payload.response.UserResponse;
import com.example.nikfrvSpring.repository.RoleRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.util.JwtUtil;
import com.example.nikfrvSpring.util.RoleConstants;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public UserService (UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;

        this.customUserDetailsService = customUserDetailsService;
    }

    private UserResponse mapToResponse(User user){
        List<String> roles = user.getRoles().stream()
                .map(Role::getRoleName)
                .toList();
        return new UserResponse(user.getUsername(), user.getPassword(), roles);
    }


    public List<UserResponse> getAllUsers(){
        log.info("Getting all users");
        return userRepository.findAll().stream().map(this::mapToResponse).toList();
    }


    public void saveUser(UserRequest userRequest) throws UserAlreadyExistsException {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.email());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setPassword(passwordEncoder.encode(userRequest.password()));

        Role userRole = roleRepository.findByRoleName(RoleConstants.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(List.of(userRole));

        if (userRequest.isAdmin()) {
            Role adminRole = roleRepository.findByRoleName(RoleConstants.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            user.setRoles(List.of(userRole, adminRole));
        }

        userRepository.save(user);
        log.info("User saved: {}", user.getUsername());
    }

    public String authenticateUser(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.email()).orElse(null);

        if (user != null && passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            log.info("User authenticated: {}", user.getUsername());

            List<String> roles = user.getRoles().stream()
                    .map(Role::getRoleName)
                    .toList();

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

            return jwtUtil.generateToken(userDetails, roles);
        }
        return null;
    }

    public void promoteToAdmin(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User not found"));

        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                .orElseThrow(()-> new RuntimeException("Admin role not found"));

        if(!user.getRoles().contains(adminRole)){
            user.getRoles().add(adminRole);
            userRepository.save(user);
            log.info("User with ID {} promoted to Admin", userId);
        }else {
            log.info("User with ID {} is already an Admin", userId);
        }
    }



    public Long removeUser (Long id){
        userRepository.deleteById(id);
        log.info("User removed with ID: {}", id);
        return id;
    }


}
