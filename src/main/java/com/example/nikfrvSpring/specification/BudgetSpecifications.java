package com.example.nikfrvSpring.specification;

import com.example.nikfrvSpring.entity.Budget;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class BudgetSpecifications {

    public static Specification<Budget> withUserId(Long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Budget> withinDateTimeRange(LocalDateTime startDateTime,
                                                            LocalDateTime endDateTime) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("budgetCreationDate"), startDateTime, endDateTime);
        };
    }
}
