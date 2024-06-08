package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Budget;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.BudgetNotFoundException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.BudgetRequest;
import com.example.nikfrvSpring.payload.response.BudgetResponse;
import com.example.nikfrvSpring.repository.BudgetRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
        budget.setBudgetName(budgetRequest.budgetName());
        budget.setBudgetSum(budgetRequest.budgetSum());
        budgetRepository.save(budget);
    }

    public void remakeBudget(BudgetRequest budgetRequest, Long id){
        Budget budget = budgetRepository.findById(id).orElseThrow(
                ()-> new BudgetNotFoundException("Budget with id" + id + "not found"));

        budget.setBudgetName(budgetRequest.budgetName());
        budget.setBudgetSum(budgetRequest.budgetSum());
        budgetRepository.save(budget);
    }

    public BudgetResponse getBudgetById (Long id){
        Budget budget = budgetRepository.findById(id).orElseThrow(
                ()-> new BudgetNotFoundException("Budget with id " +id+ " not found"));

        return new BudgetResponse(
                budget.getBudgetName(),
                budget.getBudgetSum()
        );
    }

    public List<BudgetResponse> getAllBudgetsById(Long userId){
        Optional<User> user =  userRepository.findById(userId);
        if (user.isEmpty()){
            throw new UserNotFoundException("User with id" + userId + "not found");
        }

        Optional<Budget> budgets = budgetRepository.findAllByUserId(userId);
        if (budgets.isEmpty()){
            throw new BudgetNotFoundException("No budgets found for user with id " + userId);
        }

        return budgets.stream()
                .map(budget -> new BudgetResponse(
                        budget.getBudgetName(),
                        budget.getBudgetSum()))
                .collect(Collectors.toList());
    }

    public void removeBudget(Long id){
        Budget budget = budgetRepository.findById(id).orElseThrow(
                ()-> new BudgetNotFoundException("Budget with id" +id+ "not found"));
        budgetRepository.delete(budget);
    }
}
