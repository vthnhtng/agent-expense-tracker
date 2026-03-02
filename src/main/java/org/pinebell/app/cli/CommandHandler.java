package org.pinebell.app.cli;

public interface CommandHandler {
    void execute(String[] args);
    String getCommandName();
}
