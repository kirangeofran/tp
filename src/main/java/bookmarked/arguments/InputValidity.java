package bookmarked.arguments;

import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.InvalidStringException;

public class InputValidity {
    private String[] splitArgument;
    private final String newItem;
    private final String commandString;
    private final String argumentString;



    public InputValidity(String commandString, String newItem, String argumentString) {
        this.commandString = commandString;
        this.newItem = newItem;
        this.argumentString = argumentString;
    }


    public void checkInputValidity()
            throws InvalidStringException, EmptyArgumentsException {

        String[] splitParts = this.newItem.split(this.commandString);
        if (splitParts.length < 1) {
            throw new InvalidStringException();
        }

        this.splitArgument = splitParts[1].split(this.argumentString);

        if (this.splitArgument.length < 2 || this.splitArgument[1].isBlank()) {
            throw new InvalidStringException();
        }

        if (this.splitArgument[0].isBlank()) {
            throw new EmptyArgumentsException();
        }
    }

    public String[] getSplitArgument() {
        return splitArgument;
    }
}
