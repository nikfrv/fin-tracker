package com.example.nikfrvSpring.payload.request;

public record UserRequest(String username,String email, String password, boolean isAdmin) {
}
