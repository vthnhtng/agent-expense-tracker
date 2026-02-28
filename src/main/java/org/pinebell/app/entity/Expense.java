package org.pinebell.app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Expense(
    UUID id,
    BigDecimal amount,
    String currency,
    ExpenseCategory category,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
