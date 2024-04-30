package com.example.nikfrvSpring.controller;

import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.payload.request.TransactionRequest;
import com.example.nikfrvSpring.payload.response.TransactionResponse;
import com.example.nikfrvSpring.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final  TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transactions")
    public ResponseEntity createTransaction (@RequestBody TransactionRequest transactionRequest,
                                             @RequestParam Long userId  ){
        try {
            transactionService.saveTransaction(transactionRequest, userId);
            return ResponseEntity.ok().body("Transaction saved!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/transactions")
    public ResponseEntity updateTransaction (@RequestParam Long id){
        try {
            transactionService.remakeTransaction(id);
            return ResponseEntity.ok().body("Transaction successfully updated");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error occurred");
        }
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> findAllTransactions(@RequestParam Long userId){
        return transactionService.getAllTransactionsById(userId);
    }
}
