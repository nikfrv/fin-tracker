package com.example.nikfrvSpring.controller;

import com.example.nikfrvSpring.payload.request.BudgetRequest;
import com.example.nikfrvSpring.payload.response.BudgetResponse;
import com.example.nikfrvSpring.service.BudgetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public void createBudget(@RequestBody BudgetRequest budgetRequest,
                             @RequestParam Long userId){
        budgetService.saveBudget(budgetRequest, userId);
    }

    @PutMapping
    public void updateBudget(@RequestBody BudgetRequest budgetRequest,
                             @RequestParam Long id){
        budgetService.remakeBudget(budgetRequest, id);
    }

    @GetMapping
    public List<BudgetResponse> findAllBudgets(@RequestParam Long userId){
        return budgetService.getAllBudgetsById(userId);
    }

    @GetMapping("/budget")
    public BudgetResponse findBudgetById(@RequestParam Long id){
        return budgetService.getBudgetById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBudget (@PathVariable Long id){budgetService.removeBudget(id);}
}
