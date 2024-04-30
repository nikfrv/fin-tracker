package com.example.nikfrvSpring.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private int statusCode;
    private LocalDateTime localDateTime;
    private String message;
    private String description;
}