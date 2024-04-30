package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.payload.request.TransactionRequest;
import com.example.nikfrvSpring.payload.response.TransactionResponse;
import com.example.nikfrvSpring.repository.TransactionRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public Transaction saveTransaction(TransactionRequest transactionRequest, Long userId){
        User user = userRepository.findById(userId).get();
        Transaction transaction = new Transaction();
        transaction.setTransactionDateAndTime(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setType(TransactionType.valueOf(transactionRequest.type()));
        transaction.setTransactionSum(transactionRequest.transactionSum());
        return transactionRepository.save(transaction);
    }

    public Transaction remakeTransaction(Long id){
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction != null) {
            transaction.setTransactionDateAndTime(transaction.getTransactionDateAndTime());
            transaction.setType(transaction.getType());
            transaction.setTransactionSum(transaction.getTransactionSum());
            transaction = transactionRepository.save(transaction);
        }
        return transaction;
    }

    public List<TransactionResponse> getAllTransactionsById(Long userId){
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        for(Transaction transaction : transactions){
            TransactionResponse transactionResponse = new TransactionResponse(
                    transaction.getTransactionDateAndTime(),
                    transaction.getType(),
                    transaction.getTransactionSum());
            transactionResponses.add(transactionResponse);
        }
        return transactionResponses;
    }
}
