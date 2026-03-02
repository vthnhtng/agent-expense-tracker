package org.pinebell.app.cli;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;

public class ListCommandHandler implements CommandHandler {
    private final String commandName = "list";
    private final Options options;

    public ListCommandHandler() {
		options = new Options();

        options.addOption(Option.builder()
                .longOpt("time")
                .hasArg()
                .required()
                .desc("Filter by date (dd-MM-yyyy)")
                .build());
    }

	@Override
	public void execute(String[] args) {
		System.out.println("Listing expenses");
	}

    @Override
    public String getCommandName() {
        return commandName;
    }
}
