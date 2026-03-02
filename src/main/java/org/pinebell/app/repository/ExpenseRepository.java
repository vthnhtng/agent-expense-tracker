package org.pinebell.app.repository;

import java.time.LocalDate;
import java.util.List;

import org.pinebell.app.domain.Expense;

public interface ExpenseRepository {
    void save(Expense expense);
    List<Expense> findAll();
    List<Expense> findByDate(LocalDate date);
}
