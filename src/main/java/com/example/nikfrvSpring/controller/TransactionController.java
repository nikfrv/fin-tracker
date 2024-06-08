package com.example.nikfrvSpring.controller;

import com.example.nikfrvSpring.payload.request.TransactionRequest;
import com.example.nikfrvSpring.payload.response.TransactionResponse;
import com.example.nikfrvSpring.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public void createTransaction(@RequestBody TransactionRequest transactionRequest,
                                  @RequestParam Long userId) {
        transactionService.saveTransaction(transactionRequest, userId);
    }

    @PutMapping
    public void updateTransaction(@RequestBody TransactionRequest transactionRequest,
                                  @RequestParam Long id) {
        transactionService.remakeTransaction(transactionRequest, id);
    }

    @GetMapping
    public List<TransactionResponse> findAllTransactions(@RequestParam Long userId) {
        return transactionService.getAllTransactionsByUserId(userId);
    }

    @GetMapping("/transaction")
    public TransactionResponse inspectTransaction(@RequestParam Long id){
        return transactionService.getTransactionById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id){
        transactionService.removeTransaction(id);
    }
}
