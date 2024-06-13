package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import com.example.nikfrvSpring.repository.BudgetRepository;
import com.example.nikfrvSpring.repository.TransactionRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.specification.BudgetSpecifications;
import com.example.nikfrvSpring.specification.TransactionSpecifications;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    private final BudgetRepository budgetRepository;

    private final UserRepository userRepository;

    public ReportService(TransactionRepository transactionRepository,
                         BudgetRepository budgetRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getIncomeTransactionsByUserIdAndPeriod(Long userId,
                                                                    LocalDateTime startDateTime,
                                                                    LocalDateTime endDateTime) {

        Specification<Transaction> specification = Specification.where(TransactionSpecifications.withUserId(userId))
                .and(TransactionSpecifications.withType(TransactionType.INCOME)
                        .and(TransactionSpecifications.withinDateTimeRange(startDateTime, endDateTime)));

        return transactionRepository.findAll(specification);
    }

    public List<Transaction> getExpenseTransactionsByUserIdAndPeriod(Long userId,
                                                                     LocalDateTime startDateTime,
                                                                     LocalDateTime endDateTime) {
        Specification<Transaction> specification = Specification.where(TransactionSpecifications.withUserId(userId))
                .and(TransactionSpecifications.withType(TransactionType.EXPENSE)
                        .and(TransactionSpecifications.withinDateTimeRange(startDateTime, endDateTime)));

        return transactionRepository.findAll(specification);
    }

    public List<Budget> getBudgetsByUserIdAndPeriod(Long userId,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime) {
        Specification<Budget> specification = Specification.where(BudgetSpecifications.withUserId(userId)
                .and(BudgetSpecifications.withinDateTimeRange(startDateTime, endDateTime)));

        return budgetRepository.findAll(specification);
    }
}
