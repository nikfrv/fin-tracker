package com.example.nikfrvSpring.payload.request;

import java.math.BigDecimal;

public record BudgetRequest(String budgetName, BigDecimal budgetSum) {
}
