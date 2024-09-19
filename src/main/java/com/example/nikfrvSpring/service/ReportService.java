package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import com.example.nikfrvSpring.repository.BudgetRepository;
import com.example.nikfrvSpring.repository.TransactionRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.specification.BudgetSpecifications;
import com.example.nikfrvSpring.specification.TransactionSpecifications;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
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
        List<Transaction> incomeTransactions = transactionRepository.findAll(specification);
        log.info("Retrieved {} income transactions for user {} within the period {} - {}",
                incomeTransactions.size(), userId, startDateTime, endDateTime);
        return incomeTransactions;
    }

    public List<Transaction> getExpenseTransactionsByUserIdAndPeriod(Long userId,
                                                                     LocalDateTime startDateTime,
                                                                     LocalDateTime endDateTime) {
        Specification<Transaction> specification = Specification.where(TransactionSpecifications.withUserId(userId))
                .and(TransactionSpecifications.withType(TransactionType.EXPENSE)
                        .and(TransactionSpecifications.withinDateTimeRange(startDateTime, endDateTime)));

        List<Transaction> expenseTransactions = transactionRepository.findAll(specification);
        log.info("Retrieved {} expense transactions for user {} within the period {} - {}",
                expenseTransactions.size(), userId, startDateTime, endDateTime);
        return expenseTransactions;
    }

    public List<Budget> getBudgetsByUserIdAndPeriod(Long userId,
                                                    LocalDateTime startDateTime,
                                                    LocalDateTime endDateTime) {
        Specification<Budget> specification = Specification.where(BudgetSpecifications.withUserId(userId)
                .and(BudgetSpecifications.withinDateTimeRange(startDateTime, endDateTime)));
        List<Budget> budgets = budgetRepository.findAll(specification);
        log.info("Retrieved {} budgets for user {} within the period {} - {}",
                budgets.size(), userId, startDateTime, endDateTime);
        return budgets;
    }
}
