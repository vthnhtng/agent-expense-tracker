package org.pinebell.app.cli;

import java.util.List;
import java.util.stream.Collectors;

public class CommandPool {
    private final List<CommandHandler> commands;

    public CommandPool(
        List<CommandHandler> commands
    ) {
        this.commands = commands;
    }

    public void execute(String command, String[] args) {
        for (CommandHandler commandHandler : commands) {
            if (commandHandler.getCommandName().equals(command)) {
                commandHandler.execute(args);
                return;
            }
        }

        System.out.println(
            "Command not found: " + command + "\n" +
            "Allowed commands: " + 
            commands.stream()
                .map(CommandHandler::getCommandName)
                .collect(Collectors.joining(", "))
        );
    }
}
