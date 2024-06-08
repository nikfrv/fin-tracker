package com.example.nikfrvSpring.repository;


import com.example.nikfrvSpring.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget>findAllByUserId(Long userId);

}


