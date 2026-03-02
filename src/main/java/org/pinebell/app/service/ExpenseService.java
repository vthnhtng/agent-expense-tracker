package org.pinebell.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.pinebell.app.domain.Expense;
import org.pinebell.app.repository.ExpenseRepository;
import org.pinebell.app.service.api.data.AddExpenseRequest;
import org.pinebell.app.service.api.data.AddExpenseResponse;

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
                LocalDateTime.now());

        try {
            expenseRepository.save(expense);
            return new AddExpenseResponse("Expense created successfully: " + expense);
        } catch (RuntimeException e) {
            return new AddExpenseResponse("Failed to create expense: " + e.getMessage());
        }
    }

    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByDate(LocalDate date) {
        return expenseRepository.findByDate(date);
    }
}
