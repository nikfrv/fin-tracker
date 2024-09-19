package com.example.nikfrvSpring.serviceTest;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import com.example.nikfrvSpring.repository.BudgetRepository;
import com.example.nikfrvSpring.repository.TransactionRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.service.ReportService;
import com.example.nikfrvSpring.specification.BudgetSpecifications;
import com.example.nikfrvSpring.specification.TransactionSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIncomeTransactionsByUserIdAndPeriod() {
        Long userId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endDateTime = LocalDateTime.now();
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.INCOME);
        transaction.setTransactionSum(BigDecimal.valueOf(100.0));
        transaction.setTransactionCreationDate(LocalDateTime.now());

        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(transaction));

        List<Transaction> transactions = reportService.getIncomeTransactionsByUserIdAndPeriod(userId, startDateTime, endDateTime);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(TransactionType.INCOME, transactions.get(0).getType());

        ArgumentCaptor<Specification<Transaction>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(transactionRepository, times(1)).findAll(specCaptor.capture());

        Specification<Transaction> capturedSpecification = specCaptor.getValue();
        assertNotNull(capturedSpecification);
    }

    @Test
    void testGetIncomeTransactionsByUserIdAndPeriod_EmptyList() {
        Long userId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endDateTime = LocalDateTime.now();

        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        List<Transaction> transactions = reportService.getIncomeTransactionsByUserIdAndPeriod(userId, startDateTime, endDateTime);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());

        ArgumentCaptor<Specification<Transaction>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(transactionRepository, times(1)).findAll(specCaptor.capture());

        Specification<Transaction> capturedSpecification = specCaptor.getValue();
        assertNotNull(capturedSpecification);
    }

    @Test
    void testGetExpenseTransactionsByUserIdAndPeriod() {
        Long userId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endDateTime = LocalDateTime.now();
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.EXPENSE);
        transaction.setTransactionSum(BigDecimal.valueOf(50.0));
        transaction.setTransactionCreationDate(LocalDateTime.now());

        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(transaction));

        List<Transaction> transactions = reportService.getExpenseTransactionsByUserIdAndPeriod(userId, startDateTime, endDateTime);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(TransactionType.EXPENSE, transactions.get(0).getType());

        ArgumentCaptor<Specification<Transaction>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(transactionRepository, times(1)).findAll(specCaptor.capture());

        Specification<Transaction> capturedSpecification = specCaptor.getValue();
        assertNotNull(capturedSpecification);
    }

    @Test
    void testGetExpenseTransactionsByUserIdAndPeriod_EmptyList() {
        Long userId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endDateTime = LocalDateTime.now();

        when(transactionRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        List<Transaction> transactions = reportService.getExpenseTransactionsByUserIdAndPeriod(userId, startDateTime, endDateTime);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());

        ArgumentCaptor<Specification<Transaction>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(transactionRepository, times(1)).findAll(specCaptor.capture());

        Specification<Transaction> capturedSpecification = specCaptor.getValue();
        assertNotNull(capturedSpecification);
    }

    @Test
    void testGetBudgetsByUserIdAndPeriod() {
        Long userId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endDateTime = LocalDateTime.now();
        Budget budget = new Budget();
        budget.setBudgetName("Monthly");
        budget.setBudgetSum(BigDecimal.valueOf(5000.0));
        budget.setBudgetCreationDate(LocalDateTime.now());

        when(budgetRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(budget));

        List<Budget> budgets = reportService.getBudgetsByUserIdAndPeriod(userId, startDateTime, endDateTime);

        assertNotNull(budgets);
        assertEquals(1, budgets.size());
        assertEquals("Monthly", budgets.get(0).getBudgetName());

        ArgumentCaptor<Specification<Budget>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(budgetRepository, times(1)).findAll(specCaptor.capture());

        Specification<Budget> capturedSpecification = specCaptor.getValue();
        assertNotNull(capturedSpecification);
    }

    @Test
    void testGetBudgetsByUserIdAndPeriod_EmptyList() {
        Long userId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endDateTime = LocalDateTime.now();

        when(budgetRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        List<Budget> budgets = reportService.getBudgetsByUserIdAndPeriod(userId, startDateTime, endDateTime);

        assertNotNull(budgets);
        assertTrue(budgets.isEmpty());


        ArgumentCaptor<Specification<Budget>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(budgetRepository, times(1)).findAll(specCaptor.capture());

        Specification<Budget> capturedSpecification = specCaptor.getValue();
        assertNotNull(capturedSpecification);
    }
}
