package com.example.nikfrvSpring.specification;

import com.example.nikfrvSpring.entity.Transaction;
import com.example.nikfrvSpring.entity.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TransactionSpecifications {
    public static Specification<Transaction> withUserId(Long userId) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Transaction> withType(TransactionType type) {
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("type"), type));
    }

    public static Specification<Transaction> withinDateTimeRange(LocalDateTime startDateTime,
                                                                 LocalDateTime endDateTime) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("transactionCreationDate"), startDateTime, endDateTime);
        };
    }


}
