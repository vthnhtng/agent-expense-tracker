package org.pinebell.app.service;

import java.time.LocalDate;
import java.util.List;

import org.pinebell.app.domain.Expense;
import org.pinebell.app.service.api.data.AddExpenseRequest;
import org.pinebell.app.service.api.data.AddExpenseResponse;

public interface ExpenseService {
    AddExpenseResponse createExpense(AddExpenseRequest request);
    List<Expense> getExpenses();
    List<Expense> getExpensesByDate(LocalDate date);
}