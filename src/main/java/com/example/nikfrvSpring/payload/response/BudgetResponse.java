package com.example.nikfrvSpring.payload.response;

import java.math.BigDecimal;

public record BudgetResponse(String budgetName, BigDecimal budgetSum) {
}
