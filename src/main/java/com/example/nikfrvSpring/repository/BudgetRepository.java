package com.example.nikfrvSpring.repository;


import com.example.nikfrvSpring.entity.Budget;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findAllByUserId(Long userId);
    List<Budget> findAll(Specification<Budget> specification);
}


