package org.pinebell.app;

import java.util.ArrayList;

import org.pinebell.app.cli.CommandHandler;
import org.pinebell.app.cli.CommandPool;
import org.pinebell.app.cli.AddCommandHandler;
import org.pinebell.app.cli.ListCommandHandler;
import org.pinebell.app.repository.CsvExpenseRepository;
import org.pinebell.app.repository.ExpenseRepository;
import org.pinebell.app.service.ExpenseService;
import org.pinebell.app.service.ExpenseServiceImpl;

public class App {
    public static void main(String[] args) {
        ExpenseRepository expenseRepository = new CsvExpenseRepository();
        ExpenseService expenseService = new ExpenseServiceImpl(expenseRepository);

        var commands = new ArrayList<CommandHandler>();
        commands.add(new AddCommandHandler(expenseService));
        commands.add(new ListCommandHandler(expenseService));

        CommandPool commandPool = new CommandPool(commands);

        commandPool.execute(args[0], args);
    }
}