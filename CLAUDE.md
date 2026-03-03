# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A Java CLI expense tracker application that stores expenses in CSV format. Uses Java 17 with Maven. Export cli tool for AI agent usage

## Build Commands

```bash
mvn compile          # Compile the project
mvn test             # Run all tests
mvn test -Dtest=AppTest  # Run a specific test class
mvn package          # Build shaded JAR (creates target/agent-expense-tracker-1.0-SNAPSHOT.jar)
mvn exec:java        # Run directly via Maven
```

To run the built JAR:
```bash
java -jar target/agent-expense-tracker-1.0-SNAPSHOT.jar <command> [options]
```

## Architecture

```
src/main/java/org/pinebell/app/
├── App.java                    # Entry point, wires up CommandPool with handlers
├── cli/                        # Command-line interface layer
│   ├── CommandHandler.java     # Interface for command handlers
│   ├── CommandPool.java        # Dispatches commands to appropriate handler
│   ├── AddCommandHandler.java  # Handles 'add' command
│   └── ListCommandHandler.java # Handles 'list' command
├── domain/                     # Domain entities
│   ├── Expense.java            # Expense record (UUID, amount, currency, category, description, timestamps)
│   └── ExpenseCategory.java    # Enum: FOOD, RENT, TRANSPORTATION, UTILITIES, ENTERTAINMENT, OTHER
├── repository/                 # Data persistence layer
│   ├── ExpenseRepository.java  # Interface
│   └── CsvExpenseRepository.java # CSV file implementation (stores in ~/my_expenses.csv)
└── service/                    # Business logic layer
    ├── ExpenseService.java
    └── api/data/               # Request/Response DTOs
        ├── AddExpenseRequest.java
        └── AddExpenseResponse.java
```

## Key Patterns

- **Command Pattern**: `CommandHandler` interface with `CommandPool` dispatcher. To add a new CLI command, implement `CommandHandler` and register it in `App.java`.
- **Repository Pattern**: `ExpenseRepository` interface with `CsvExpenseRepository` implementation. Uses Apache Commons CSV.
- **Dependency Injection**: Services/repositories are instantiated manually in `App.java` and passed via constructor.

## CLI Usage

```bash
# Add expense
java -jar target/agent-expense-tracker-1.0-SNAPSHOT.jar add --amount 100 --currency VND --category FOOD --description "Lunch"

# List expenses
java -jar target/agent-expense-tracker-1.0-SNAPSHOT.jar list --time 01-03-2026
```

## Data Storage

Expenses are stored in CSV format at `~/my_expenses.csv` with headers: id, amount, currency, category, description, createdAt, updatedAt