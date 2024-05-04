package com.example.nikfrvSpring.exceptions;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message) {super(message);}
}
