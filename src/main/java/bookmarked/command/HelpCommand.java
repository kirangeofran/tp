package bookmarked.command;

import bookmarked.ui.Ui;

public class HelpCommand extends Command {
    /**
     * Constructs a HelpCommand object.
     */
    public HelpCommand() {

    }

    /**
     * Executes the help command and prints all the available command with its format.
     */
    @Override
    public void handleCommand() {
        Ui.printHelpMessage();
    }
}
