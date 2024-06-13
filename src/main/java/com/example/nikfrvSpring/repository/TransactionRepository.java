package com.example.nikfrvSpring.repository;

import com.example.nikfrvSpring.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUserId(Long userId);
    List<Transaction> findAll(Specification<Transaction> specification);
}
