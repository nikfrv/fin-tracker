package com.example.nikfrvSpring.controller;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/income")
    public List<Transaction> findAllIncomes(@RequestParam Long userId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return reportService.getIncomeTransactionsByUserIdAndPeriod(userId, startDateTime, endDateTime);
    }

    @GetMapping("/expense")
    public List<Transaction> findAllExpenses(@RequestParam Long userId,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return reportService.getExpenseTransactionsByUserIdAndPeriod(userId, startDateTime, endDateTime);
    }

    @GetMapping("/budgets")
    public List<Budget> findAllBudgets(@RequestParam Long userId,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDateTime) {
        return reportService.getBudgetsByUserIdAndPeriod(userId, startDateTime, endDateTime);
    }
}
