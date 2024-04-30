package com.example.nikfrvSpring.service;

import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.TransactionNotFoundException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.TransactionRequest;
import com.example.nikfrvSpring.payload.response.TransactionResponse;
import com.example.nikfrvSpring.repository.TransactionRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public void saveTransaction(TransactionRequest transactionRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User with id: " + userId + " not found"));

        Transaction transaction = new Transaction();
        transaction.setTransactionDateAndTime(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setType(TransactionType.valueOf(transactionRequest.type()));
        transaction.setTransactionSum(transactionRequest.transactionSum());
        transactionRepository.save(transaction);
    }

    public void remakeTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Transaction with id: " + id + " not found"));

        transaction.setTransactionDateAndTime(transaction.getTransactionDateAndTime());
        transaction.setType(transaction.getType());
        transaction.setTransactionSum(transaction.getTransactionSum());
        transactionRepository.save(transaction);

    }

    public List<TransactionResponse> getAllTransactionsById(Long userId) {
        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);

        return transactions.stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getTransactionDateAndTime(),
                        transaction.getType(),
                        transaction.getTransactionSum()))
                .collect(Collectors.toList());
    }
}
