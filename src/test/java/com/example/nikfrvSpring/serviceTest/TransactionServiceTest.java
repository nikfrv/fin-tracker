package com.example.nikfrvSpring.serviceTest;

import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import com.example.nikfrvSpring.entity.User;
import com.example.nikfrvSpring.exceptions.TransactionNotFoundException;
import com.example.nikfrvSpring.exceptions.UserNotFoundException;
import com.example.nikfrvSpring.payload.request.TransactionRequest;
import com.example.nikfrvSpring.payload.response.TransactionResponse;
import com.example.nikfrvSpring.repository.TransactionRepository;
import com.example.nikfrvSpring.repository.UserRepository;
import com.example.nikfrvSpring.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTransaction_UserExists() {
        Long userId = 1L;
        TransactionRequest transactionRequest = new TransactionRequest("INCOME", BigDecimal.valueOf(100.0));
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        transactionService.saveTransaction(transactionRequest, userId);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository, times(1)).save(transactionCaptor.capture());
        verify(userRepository, times(1)).findById(userId);

        Transaction savedTransaction = transactionCaptor.getValue();
        assertNotNull(savedTransaction);
        assertEquals(TransactionType.INCOME, savedTransaction.getType());
        assertEquals(transactionRequest.transactionSum(), savedTransaction.getTransactionSum());
        assertNotNull(savedTransaction.getTransactionCreationDate());
        assertEquals(user, savedTransaction.getUser());
    }

    @Test
    void testSaveTransaction_UserNotFound() {
        Long userId = 1L;
        TransactionRequest transactionRequest = new TransactionRequest("INCOME", BigDecimal.valueOf(100.0));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transactionService.saveTransaction(transactionRequest, userId));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetTransactionById_TransactionExists() {
        Long transactionId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction();
        transaction.setTransactionCreationDate(now);
        transaction.setTransactionSum(BigDecimal.valueOf(100.0));
        transaction.setType(TransactionType.INCOME);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransactionById(transactionId);

        assertNotNull(response);
        assertEquals(now, response.transactionCreationDate());
        assertEquals(TransactionType.INCOME, response.type());
        assertEquals(BigDecimal.valueOf(100.0), response.transactionSum());
        verify(transactionRepository, times(1)).findById(transactionId);
    }


    @Test
    void testGetTransactionById_TransactionNotFound() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionById(transactionId));
        verify(transactionRepository, times(1)).findById(transactionId);
    }

    @Test
    void testRemakeTransaction_TransactionExists() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        TransactionRequest transactionRequest = new TransactionRequest("EXPENSE",  BigDecimal.valueOf(50.0));

        transactionService.remakeTransaction(transactionRequest, transactionId);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testRemakeTransaction_TransactionNotFound() {
        Long transactionId = 1L;
        TransactionRequest transactionRequest = new TransactionRequest("EXPENSE",  BigDecimal.valueOf(50.0));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.remakeTransaction(transactionRequest, transactionId));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void testGetAllTransactionsByUserId_UserExists() {

        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Transaction transaction = new Transaction();
        transaction.setTransactionCreationDate(LocalDateTime.now());
        transaction.setTransactionSum(BigDecimal.valueOf(100.0));
        transaction.setType(TransactionType.INCOME);
        when(transactionRepository.findAllByUserId(userId)).thenReturn(List.of(transaction));

        List<TransactionResponse> responses = transactionService.getAllTransactionsByUserId(userId);

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size()); // Проверка размера списка
        TransactionResponse response = responses.get(0);
        assertEquals(transaction.getTransactionCreationDate(), response.transactionCreationDate());
        assertEquals(transaction.getType(), response.type());
        assertEquals(transaction.getTransactionSum(), response.transactionSum());
        verify(transactionRepository, times(1)).findAllByUserId(userId);
    }


    @Test
    void testGetAllTransactionsByUserId_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> transactionService.getAllTransactionsByUserId(userId));
    }

    @Test
    void testGetAllTransactionsByUserId_NoTransactions() {
        Long userId = 1L;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(transactionRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getAllTransactionsByUserId(userId));
    }

    @Test
    void testRemoveTransaction_TransactionExists() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.removeTransaction(transactionId);

        verify(transactionRepository, times(1)).delete(transaction);
    }

    @Test
    void testRemoveTransaction_TransactionNotFound() {
        Long transactionId = 1L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.removeTransaction(transactionId));
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }
}
