package com.example.nikfrvSpring.repository;

import com.example.nikfrvSpring.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findAllByUserId(Long userId);

}
