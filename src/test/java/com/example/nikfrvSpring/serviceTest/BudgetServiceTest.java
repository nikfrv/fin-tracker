package com.example.nikfrvSpring.serviceTest;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.BudgetNotFoundException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.BudgetRequest;
import com.example.nikfrvSpring.payload.response.BudgetResponse;
import com.example.nikfrvSpring.repository.BudgetRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBudget_UserExists() {

        Long userId = 1L;
        BudgetRequest budgetRequest = new BudgetRequest("Monthly", BigDecimal.valueOf(5000.0));
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        budgetService.saveBudget(budgetRequest, userId);

        verify(budgetRepository, times(1)).save(any(Budget.class));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testSaveBudget_UserNotFound() {
        Long userId = 1L;
        BudgetRequest budgetRequest = new BudgetRequest("Monthly", BigDecimal.valueOf(5000.0));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> budgetService.saveBudget(budgetRequest, userId));
        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    void testRemakeBudget_BudgetExists() {
        Long budgetId = 1L;
        Budget budget = new Budget();
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        BudgetRequest budgetRequest = new BudgetRequest("Annual", BigDecimal.valueOf(60000.0));

        budgetService.remakeBudget(budgetRequest, budgetId);

        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void testRemakeBudget_BudgetNotFound() {
        Long budgetId = 1L;
        BudgetRequest budgetRequest = new BudgetRequest("Annual", BigDecimal.valueOf(60000.0));
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.remakeBudget(budgetRequest, budgetId));
        verify(budgetRepository, never()).save(any(Budget.class));
    }

    @Test
    void testGetBudgetById_BudgetExists() {
        Long budgetId = 1L;
        Budget budget = new Budget();
        budget.setBudgetCreationDate(LocalDateTime.now());
        budget.setBudgetName("Monthly");
        budget.setBudgetSum(BigDecimal.valueOf(5000.0));

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        BudgetResponse response = budgetService.getBudgetById(budgetId);

        assertNotNull(response);
        assertEquals(budget.getBudgetCreationDate(), response.budgetCreationDate());
        assertEquals(budget.getBudgetName(), response.budgetName());
        assertEquals(budget.getBudgetSum(), response.budgetSum());
        verify(budgetRepository, times(1)).findById(budgetId);
    }

    @Test
    void testGetBudgetById_BudgetNotFound() {
        Long budgetId = 1L;
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.getBudgetById(budgetId));
        verify(budgetRepository, times(1)).findById(budgetId);
    }

    @Test
    void testGetAllBudgetsById_UserExists() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Budget budget = new Budget();
        budget.setBudgetCreationDate(LocalDateTime.now());
        budget.setBudgetName("Monthly");
        budget.setBudgetSum(BigDecimal.valueOf(5000.0));
        when(budgetRepository.findAllByUserId(userId)).thenReturn(List.of(budget));

        List<BudgetResponse> responses = budgetService.getAllBudgetsById(userId);

        assertFalse(responses.isEmpty());
        BudgetResponse response = responses.get(0);
        assertEquals(budget.getBudgetCreationDate(), response.budgetCreationDate());
        assertEquals(budget.getBudgetName(), response.budgetName());
        assertEquals(budget.getBudgetSum(), response.budgetSum());
        verify(budgetRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testGetAllBudgetsById_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> budgetService.getAllBudgetsById(userId));
    }

    @Test
    void testGetAllBudgetsById_NoBudgets() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(budgetRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.getAllBudgetsById(userId));
    }

    @Test
    void testRemoveBudget_BudgetExists() {
        Long budgetId = 1L;
        Budget budget = new Budget();
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));

        budgetService.removeBudget(budgetId);

        verify(budgetRepository, times(1)).delete(budget);
    }

    @Test
    void testRemoveBudget_BudgetNotFound() {
        Long budgetId = 1L;
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        assertThrows(BudgetNotFoundException.class, () -> budgetService.removeBudget(budgetId));
        verify(budgetRepository, never()).delete(any(Budget.class));
    }

}
