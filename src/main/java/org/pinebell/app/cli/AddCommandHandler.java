package org.pinebell.app.cli;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.pinebell.app.domain.ExpenseCategory;
import org.pinebell.app.service.ExpenseService;
import org.pinebell.app.service.api.data.AddExpenseRequest;
import org.pinebell.app.service.api.data.AddExpenseResponse;

public class AddCommandHandler implements CommandHandler {
    private final String commandName = "add";
    private final Options options;
    private final ExpenseService expenseService;

    public AddCommandHandler(ExpenseService expenseService) {
        this.expenseService = expenseService;
        options = new Options();

        options.addOption(Option.builder()
            .longOpt("amount")
            .hasArg()
            .required()
            .desc("Expense amount")
            .build()
        );

        options.addOption(Option.builder()
            .longOpt("currency")
            .hasArg()
            .required()
            .desc("Currency code (e.g., VND, USD, YEN, EUR, GBP, etc.)")
            .build()
        );

        options.addOption(Option.builder()
            .longOpt("category")
            .hasArg()
            .required()
            .desc("Expense category")
            .build()
        );

        options.addOption(Option.builder()
            .longOpt("description")
            .hasArg()
            .desc("Expense description")
            .build()
        );

        options.addOption(Option.builder()
            .longOpt("createdAt")
            .hasArg()
            .desc("Create at date (dd-MM-yyyy HH:mm)")
            .build()
        );
    }

    @Override
    public void execute(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            String[] optionArgs = Arrays.copyOfRange(args, 1, args.length);
            CommandLine cmd = parser.parse(options, optionArgs);

            String amount = cmd.getOptionValue("amount");
            String currency = cmd.getOptionValue("currency");
            String category = cmd.getOptionValue("category");
            String description = cmd.getOptionValue("description");
            String createdAt = cmd.getOptionValue("createdAt");

            AddExpenseRequest request = new AddExpenseRequest(
                new BigDecimal(amount),
                currency,
                ExpenseCategory.valueOf(category),
                description,
                createdAt
            );

            AddExpenseResponse response = expenseService.createExpense(request);
            System.out.println(response.message());
        } catch (ParseException e) {
            System.out.println("Failed to parse options for 'add' command: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error saving expense: " + e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return commandName;
    }
}