package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.BudgetNotFoundException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.BudgetRequest;
import com.example.nikfrvSpring.payload.response.BudgetResponse;
import com.example.nikfrvSpring.repository.BudgetRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class BudgetService {
    private final BudgetRepository budgetRepository;

    private final UserRepository userRepository;

    public BudgetService(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    public void saveBudget(BudgetRequest budgetRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id: " + userId + " not found"));

        Budget budget = new Budget();
        budget.setUser(user);
        budget.setBudgetCreationDate(LocalDateTime.now());
        budget.setBudgetName(budgetRequest.budgetName());
        budget.setBudgetSum(budgetRequest.budgetSum());
        budgetRepository.save(budget);
        log.info("Budget saved with ID {} for user {}", budget.getId(), userId);
    }

    public void remakeBudget(BudgetRequest budgetRequest, Long id){
        Budget budget = budgetRepository.findById(id).orElseThrow(
                ()-> new BudgetNotFoundException("Budget with id" + id + "not found"));

        budget.setBudgetCreationDate(LocalDateTime.now());
        budget.setBudgetName(budgetRequest.budgetName());
        budget.setBudgetSum(budgetRequest.budgetSum());
        budgetRepository.save(budget);
        log.info("Remade budget with id {}", id);
    }

    public BudgetResponse getBudgetById (Long id){
        Budget budget = budgetRepository.findById(id).orElseThrow(
                ()-> new BudgetNotFoundException("Budget with id " +id+ " not found"));

        log.info("Retrieved budget with id {}", id);
        return new BudgetResponse(
                budget.getBudgetCreationDate(),
                budget.getBudgetName(),
                budget.getBudgetSum()
        );
    }

    public List<BudgetResponse> getAllBudgetsById(Long userId){
        Optional<User> user =  userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User with id" + userId + "not found");
        }

        List<Budget> budgets = budgetRepository.findAllByUserId(userId);
        if (budgets.isEmpty()){
            throw new BudgetNotFoundException("No budgets found for user with id " + userId);
        }

        log.info("Retrieved {} budgets for user {}", budgets.size(), userId);
        return budgets.stream()
                .map(budget -> new BudgetResponse(
                        budget.getBudgetCreationDate(),
                        budget.getBudgetName(),
                        budget.getBudgetSum()))
                .collect(Collectors.toList());
    }

    public void removeBudget(Long id){
        Budget budget = budgetRepository.findById(id).orElseThrow(
                ()-> new BudgetNotFoundException("Budget with id" +id+ "not found"));
        budgetRepository.delete(budget);
        log.info("Removed budget with id {}", id);
    }
}
