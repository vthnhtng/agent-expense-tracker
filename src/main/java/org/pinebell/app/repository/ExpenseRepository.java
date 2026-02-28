package org.pinebell.app.repository;

import java.util.List;

import org.pinebell.app.entity.Expense;

public interface ExpenseRepository {
    public void create(Expense expense) throws RuntimeException;
    public List<Expense> findAll() throws RuntimeException;
    // public Expense findById(UUID id);
    // public void update(Expense expense);
}
