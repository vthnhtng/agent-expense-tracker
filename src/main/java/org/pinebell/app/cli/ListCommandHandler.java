package org.pinebell.app.cli;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.pinebell.app.domain.Expense;
import org.pinebell.app.service.ExpenseService;

public class ListCommandHandler implements CommandHandler {
    private final String commandName = "list";
    private final Options options;
    private final ExpenseService expenseService;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ListCommandHandler(ExpenseService expenseService) {
        this.expenseService = expenseService;
        options = new Options();

        options.addOption(Option.builder()
                .longOpt("time")
                .hasArg()
                .desc("Filter by date (dd-MM-yyyy)")
                .build());
    }

    @Override
    public void execute(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            String[] optionArgs = Arrays.copyOfRange(args, 1, args.length);
            CommandLine cmd = parser.parse(options, optionArgs);

            String time = cmd.getOptionValue("time");
            List<Expense> expenses;

            if (time != null) {
                LocalDate date = parseDate(time);
                expenses = expenseService.getExpensesByDate(date);
            } else {
                expenses = expenseService.getExpenses();
            }

            if (expenses.isEmpty()) {
                System.out.println("No expenses found.");
            } else {
                System.out.println("Expenses:");
                for (Expense expense : expenses) {
                    System.out.println("  " + expense);
                }
            }
        } catch (ParseException e) {
            System.out.println("Failed to parse options for 'list' command: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: dd-MM-yyyy", e);
        }
    }
}