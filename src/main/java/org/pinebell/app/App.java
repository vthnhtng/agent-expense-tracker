package org.pinebell.app;

import java.util.ArrayList;

import org.pinebell.app.cli.CommandHandler;
import org.pinebell.app.cli.CommandPool;
import org.pinebell.app.cli.AddCommandHandler;
import org.pinebell.app.cli.ListCommandHandler;

public class App {
    public static void main(String[] args) {

        var commands = new ArrayList<CommandHandler>();

        commands.add(new AddCommandHandler());
        commands.add(new ListCommandHandler());

        CommandPool commandPool = new CommandPool(commands);

        commandPool.execute(args[0], args);
    }
}
