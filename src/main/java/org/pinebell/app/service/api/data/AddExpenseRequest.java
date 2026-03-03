package org.pinebell.app.service.api.data;

import java.math.BigDecimal;

import org.pinebell.app.domain.ExpenseCategory;

public record AddExpenseRequest(
        BigDecimal amount,
        String currency,
        ExpenseCategory category,
        String description,
        String createdAt
){
}
