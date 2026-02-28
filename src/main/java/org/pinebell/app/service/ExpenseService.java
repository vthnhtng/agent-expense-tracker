package org.pinebell.app.service;

import org.pinebell.app.repository.ExpenseRepository;
import org.pinebell.app.service.api.data.AddExpenseRequest;
import org.pinebell.app.service.api.data.AddExpenseResponse;
import org.pinebell.app.entity.Expense;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public AddExpenseResponse createExpense(AddExpenseRequest request) {
        Expense expense = new Expense(
            UUID.randomUUID(),
            request.amount(),
            request.currency(),
            request.category(),
            request.description(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        try {
            expenseRepository.create(expense);
            return new AddExpenseResponse("Expense created successfully: " + expense.toString());
        } catch (RuntimeException e) {
            return new AddExpenseResponse("Failed to create expense: " + e.getMessage());
        }
    }

    public List<Expense> getExpenses() {
        try {
            return expenseRepository.findAll();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to get expenses: " + e.getStackTrace(), e);
        }
    }
}
