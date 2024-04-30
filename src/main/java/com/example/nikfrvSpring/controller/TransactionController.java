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
    public void updateTransaction(@RequestParam Long id) {
        transactionService.remakeTransaction(id);
    }

    @GetMapping
    public List<TransactionResponse> findAllTransactions(@RequestParam Long userId) {
        return transactionService.getAllTransactionsById(userId);
    }
}
