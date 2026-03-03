package org.pinebell.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import org.pinebell.app.domain.Expense;
import org.pinebell.app.repository.ExpenseRepository;
import org.pinebell.app.service.api.data.AddExpenseRequest;
import org.pinebell.app.service.api.data.AddExpenseResponse;

public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public AddExpenseResponse createExpense(AddExpenseRequest request) {
        LocalDateTime createdAt = parseCreatedAt(request.createdAt());
        LocalDateTime now = LocalDateTime.now();

        Expense expense = new Expense(
                UUID.randomUUID(),
                request.amount(),
                request.currency(),
                request.category(),
                request.description(),
                createdAt,
                now);

        try {
            expenseRepository.save(expense);
            return new AddExpenseResponse("Expense created successfully: " + expense);
        } catch (RuntimeException e) {
            return new AddExpenseResponse("Failed to create expense: " + e.getMessage());
        }
    }

    @Override
    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> getExpensesByDate(LocalDate date) {
        return expenseRepository.findByDate(date);
    }

    private LocalDateTime parseCreatedAt(String createdAt) {
        if (createdAt == null || createdAt.isBlank()) {
            return LocalDateTime.now();
        }

        try {
            return LocalDateTime.parse(createdAt, DATE_TIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid createdAt format. Expected format: dd-MM-yyyy HH:mm", e);
        }
    }
}