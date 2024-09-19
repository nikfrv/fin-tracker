package com.example.nikfrvSpring.payload.response;

import com.example.nikfrvSpring.entity.Role;

import java.util.List;

public record UserResponse(String username, String password, List<String> roles) {
}
