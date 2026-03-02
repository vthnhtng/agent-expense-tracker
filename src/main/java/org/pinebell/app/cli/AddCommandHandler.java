package org.pinebell.app.cli;

import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class AddCommandHandler implements CommandHandler {
    private final String commandName = "add";
    private final Options options;

    public AddCommandHandler() {
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
            .desc("Create at date (dd-MM-yyyy hh:mm)")
            .build()
        );
    }

    @Override
    public void execute(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            // Skip the command name (args[0]) and parse only the options
            String[] optionArgs = Arrays.copyOfRange(args, 1, args.length);
            CommandLine cmd = parser.parse(options, optionArgs);

            String amount = cmd.getOptionValue("amount");
            String currency = cmd.getOptionValue("currency");
            String category = cmd.getOptionValue("category");
            String description = cmd.getOptionValue("description");
            String createdAt = cmd.getOptionValue("createdAt");

            System.out.println("Add expense command:");
            System.out.println("  amount      = " + amount);
            System.out.println("  currency    = " + currency);
            System.out.println("  category    = " + category);
            System.out.println("  description = " + (description != null ? description : ""));
            System.out.println("  createdAt   = " + (createdAt != null ? createdAt : ""));
        } catch (ParseException e) {
            System.out.println("Failed to parse options for 'add' command: " + e.getMessage());
        }
    }

    @Override
    public String getCommandName() {
        return commandName;
    }
}
