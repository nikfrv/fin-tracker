package com.example.nikfrvSpring.payload.request;

import com.example.nikfrvSpring.entity.TransactionType;

import java.math.BigDecimal;

public record TransactionRequest(String type, BigDecimal transactionSum) {
}
