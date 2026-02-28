package org.pinebell.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.pinebell.app.entity.Expense;
import org.pinebell.app.entity.ExpenseCategory;
import org.pinebell.app.repository.ExpenseRepositoryCsv;
import org.pinebell.app.service.ExpenseService;
import org.pinebell.app.service.api.data.AddExpenseRequest;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

@Command(
    name = "expenses",
    mixinStandardHelpOptions = true,
    version = "expenses 1.0",
    description = "Expense tracker CLI application",
    subcommands = {
        App.CreateCommand.class,
        App.ListCommand.class
    }
)
public class App implements Runnable {

    final ExpenseService expenseService;

    public App() {
        ExpenseRepositoryCsv expenseRepository = new ExpenseRepositoryCsv();
        this.expenseService = new ExpenseService(expenseRepository);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    @Command(
        name = "create",
        description = "Create a new expense"
    )
    static class CreateCommand implements Runnable {

        @ParentCommand
        App parent;

        @Option(
            names = { "-a", "--amount" },
            required = true,
            description = "Expense amount"
        )
        BigDecimal amount;

        @Option(
            names = { "-c", "--currency" },
            required = true,
            description = "Currency code, e.g. USD"
        )
        String currency;

        @Option(
            names = { "-g", "--category" },
            required = true,
            description = "Expense category. Valid values: ${COMPLETION-CANDIDATES}"
        )
        ExpenseCategory category;

        @Parameters(
            arity = "1..*",
            description = "Expense description (one or more words)"
        )
        List<String> descriptionParts;

        @Override
        public void run() {
            String description = descriptionParts.stream().collect(Collectors.joining(" "));

            var response = parent.expenseService.createExpense(
                new AddExpenseRequest(amount, currency, category, description)
            );

            System.out.println(response.message());
        }
    }

    @Command(
        name = "list",
        aliases = { "get", "get-all" },
        description = "List all expenses"
    )
    static class ListCommand implements Runnable {

        @ParentCommand
        App parent;

        @Override
        public void run() {
            List<Expense> expenses = parent.expenseService.getExpenses();

            if (expenses.isEmpty()) {
                System.out.println("No expenses found.");
                return;
            }

            for (Expense expense : expenses) {
                System.out.println(expense.toString());
            }
        }
    }
}
