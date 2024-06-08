package com.example.nikfrvSpring.handler;

import com.example.nikfrvSpring.exceptions.BudgetNotFoundException;
import com.example.nikfrvSpring.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BudgetExceptoinHandler {

    @ExceptionHandler(BudgetNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleAuthException (BudgetNotFoundException ex, WebRequest request){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }
}
