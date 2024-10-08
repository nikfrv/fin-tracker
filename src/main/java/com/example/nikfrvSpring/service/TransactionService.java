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
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
@Log4j2
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
        transaction.setTransactionCreationDate(LocalDateTime.now());
        transaction.setUser(user);
        transaction.setType(TransactionType.valueOf(transactionRequest.type()));
        transaction.setTransactionSum(transactionRequest.transactionSum());
        transactionRepository.save(transaction);
        log.info("Transaction saved with ID {} for user with id {}", transaction.getId(), userId);
    }

    public TransactionResponse getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Transaction with id " + id + " not found"));
        log.info("Retrieved transaction with id {}", id);
        return new TransactionResponse(
                transaction.getTransactionCreationDate(),
                transaction.getType(),
                transaction.getTransactionSum()
        );
    }

    public void remakeTransaction(TransactionRequest transactionRequest, Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Transaction with id: " + id + " not found"));

        transaction.setTransactionCreationDate(LocalDateTime.now());
        transaction.setType(TransactionType.valueOf(transactionRequest.type()));
        transaction.setTransactionSum(transactionRequest.transactionSum());
        transactionRepository.save(transaction);
        log.info("Transaction remade with ID: {}", transaction.getId());
    }

    public List<TransactionResponse> getAllTransactionsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        List<Transaction> transactions = transactionRepository.findAllByUserId(userId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for user with id " + userId);
        }

        log.info("Retrieved {} transactions for user with id {}", transactions.size(), userId);
        return transactions.stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getTransactionCreationDate(),
                        transaction.getType(),
                        transaction.getTransactionSum()))
                .collect(Collectors.toList());
    }

    public void removeTransaction(Long id){
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()->
                new TransactionNotFoundException("Transaction with id " + id +" not found"));
        transactionRepository.delete(transaction);
        log.info("Transaction removed with ID: {}", id);
    }
}
