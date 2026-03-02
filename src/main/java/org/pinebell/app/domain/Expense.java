package org.pinebell.app.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record Expense(
        UUID id,
        BigDecimal amount,
        String currency,
        ExpenseCategory category,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Override
    public String toString() {
        return String.format("[%s] %s %s | %s | %s | %s",
                id.toString().substring(0, 8),
                amount,
                currency,
                category,
                description,
                createdAt.format(DISPLAY_FORMAT));
    }
}
