package com.example.nikfrvSpring.payload.response;

import com.example.nikfrvSpring.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(LocalDateTime transactionCreationDate, TransactionType type, BigDecimal transactionSum) {

}
