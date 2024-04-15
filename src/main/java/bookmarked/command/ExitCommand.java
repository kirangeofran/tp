package bookmarked.command;

import bookmarked.ui.Ui;

/**
 * Represents a command to exit the program safely.
 * This class is responsible for handling the `bye` command input by the user.
 */
public class ExitCommand extends Command {
    /**
     * Constructs an ExitCommand object.
     */
    public ExitCommand() {
    }

    /**
     * Executes the exit command, closing the program and printing a exit message.
     */
    @Override
    public void handleCommand() {
        Ui.exitProgramme();
    }
}
