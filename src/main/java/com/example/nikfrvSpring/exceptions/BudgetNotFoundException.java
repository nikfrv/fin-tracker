package com.example.nikfrvSpring.exceptions;

public class BudgetNotFoundException extends RuntimeException{
    public BudgetNotFoundException (String message){
        super(message);
    }
}
