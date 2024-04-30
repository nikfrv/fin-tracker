package com.example.nikfrvSpring.payload.response;

import com.example.nikfrvSpring.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(LocalDateTime transactionDateAndTime, TransactionType type, BigDecimal transactionSum) {

}
