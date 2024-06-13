package com.example.nikfrvSpring.payload.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BudgetResponse(LocalDateTime budgetCreationDate, String budgetName, BigDecimal budgetSum) {
}
