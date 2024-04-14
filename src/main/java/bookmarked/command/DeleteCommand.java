package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.*;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

/**
 * This class represents a command for deleting a specified quantity of books from the library's inventory.
 * It handles commands related to deleting books either by an index or by title with optional quantities.
 */
public class DeleteCommand extends Command {
    private static final int DEFAULT_QUANTITY = 1;
    private static final int MAX_QUANTITY = 1000;
    private static final String MAX_QUANTITY_STRING = "1000";
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;
    private String[] splitQuantity;
    private int quantityToDelete;
    private boolean hasQuantityArgument;
    private int inputtedIndex;

    /**
     * Constructs a DeleteCommand with the necessary context for performing deletion.
     *
     * @param newItem The command string indicating the item and possibly the quantity to delete.
     * @param listOfBooks A list of books from which the book will be deleted.
     * @param bookDataFile The file where the book data is stored and will be updated after deletion.
     */
    public DeleteCommand(String newItem, ArrayList<Book> listOfBooks, File bookDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.hasQuantityArgument = false;
    }

    /**
     * Handles the deletion command by interpreting the input and processing the deletion.
     */
    @Override
    public void handleCommand() {
        String[] newSplitBook = this.newItem.split("delete");
        if (newSplitBook.length < 1) {
            Ui.printEmptyArgumentsMessage();
            return;
        }

        try {
            processDeleteCommand(newSplitBook);
           // assert !this.listOfBooks.isEmpty() : "The current list of books should not be empty";
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        } catch (EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
        } catch (IndexOutOfListBounds e) {
            Ui.printOutOfBoundsMessage();
        } catch (NumberFormatException e) {
            Ui.printNotNumberMessage();
        }
    }

    /**
     * Processes the delete command based on the parsed arguments.
     *
     * @param newSplitBook The split parts of the command input.
     * @throws EmptyListException If the book list is empty.
     * @throws EmptyArgumentsException If there are no valid arguments following the delete command.
     * @throws IndexOutOfListBounds If the specified index is out of bounds.
     */

    public void processDeleteCommand(String[] newSplitBook)
            throws EmptyListException, EmptyArgumentsException, IndexOutOfListBounds {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        // checks if bookToDelete contains only the word "delete" or if there are only white spaces after it
        if (newSplitBook.length < 1) {
            throw new EmptyArgumentsException();
        }

        this.splitQuantity = newSplitBook[1].split(" /quantity ");
        if (this.splitQuantity[0].isBlank()) {
            throw new EmptyArgumentsException();
        }

        try {
            quantityToDelete = setQuantityToDelete();
            runDeleteCommand();
        } catch (WrongQuantityException e) {
            Ui.printBlankDeleteQuantity();
        } catch (NumberFormatException e) {
            Ui.printWrongQuantityFormat();
        } catch (MaxIntNumberException e) {
            Ui.printMaxDeleteNumberMessage();
        } catch (NegativeQuantityException e) {
            Ui.printNegativeDeleteQuantityMessage();
        } catch (TooLargeQuantityException e) {
            Ui.printDeleteFewerBooksMessage();
        } catch (IndexOutOfListBounds e) {
            Ui.printOutOfBoundsMessage();
        } catch (WrongFormatQuantityException e) {
            Ui.printNotNumberMessage();
        }
    }

    /**
     * Determines the quantity of books to delete based on the input arguments.
     *
     * @return The quantity of books to be deleted.
     * @throws WrongQuantityException If the specified quantity is incorrect.
     * @throws NumberFormatException If the quantity is not a valid number.
     * @throws MaxIntNumberException If the quantity exceeds the maximum limit.
     * @throws NegativeQuantityException If the quantity is negative.
     */
    public int setQuantityToDelete() throws WrongQuantityException, NumberFormatException,
            MaxIntNumberException, NegativeQuantityException {
        // if there is no /quantity argument
        if (newItem.contains(" /quantity")) {
            hasQuantityArgument = true;
        }

        if (hasQuantityArgument) {
            checkQuantityStringValidity();
            int quantityToDelete = Integer.parseInt(splitQuantity[1].trim());
            if (quantityToDelete <= 0) {
                throw new NegativeQuantityException();
            } else if (quantityToDelete > MAX_QUANTITY) {
                throw new MaxIntNumberException();
            }
            return quantityToDelete;
        } else {
            return DEFAULT_QUANTITY;
        }

    }

    /**
     * Validates the format of the quantity string to ensure it is a valid number within the allowed range.
     *
     * @throws WrongQuantityException If the quantity string is incorrectly formatted.
     * @throws MaxIntNumberException If the quantity exceeds the maximum allowed value.
     */
    public void checkQuantityStringValidity() throws WrongQuantityException, MaxIntNumberException {
        if (splitQuantity.length < 2 || splitQuantity[1].isBlank()) {
            throw new WrongQuantityException();
        }

        String quantityString = splitQuantity[1].trim();
        if (quantityString.length() >= 4 && !quantityString.equals(MAX_QUANTITY_STRING)) {
            // Checks if the input itself is longer than a 4 digit number, and if it's not, checks if it's any
            // other 4 digit number than 1000. 1000 is the maximum number of copies, so the deletion number can't
            // be larger than that.
            if (quantityString.matches("^[0-9]+$")) {
                // Check if the input string contains only numbers
                throw new MaxIntNumberException();
            } else {
                // Contains other symbols such as letters or special characters
                throw new NumberFormatException();
            }
        }
    }

    /**
     * Executes the deletion of a specified number of books from the inventory.
     *
     * @throws MaxIntNumberException If attempting to delete more books than are available.
     * @throws TooLargeQuantityException If the quantity to delete is larger than the current inventory.
     * @throws IndexOutOfListBounds If the specified index is not within the bounds of the book list.
     * @throws WrongFormatQuantityException If the index is not formatted correctly.
     */
    public void runDeleteCommand() throws MaxIntNumberException, TooLargeQuantityException,
            IndexOutOfListBounds, WrongFormatQuantityException {
        try {
            inputtedIndex = setInputtedIndex();
        } catch (IndexOutOfListBounds e) {
            throw new IndexOutOfListBounds();
        } catch (NumberFormatException e) {
            throw new WrongFormatQuantityException();
        }

        int listNumberIndex = (inputtedIndex - 1);
        Book inputBook = listOfBooks.get(listNumberIndex);

        int currentNumberInInventory = inputBook.getNumberInInventory();
        int currentNumberTotal = inputBook.getNumberTotal();
        int newNumberInInventory = currentNumberInInventory - quantityToDelete;
        int newNumberTotal = inputBook.getNumberTotal() - quantityToDelete;

        if (currentNumberTotal == 0 || currentNumberInInventory == 0) {
            Ui.printDeleteNoCopiesErrorException(inputBook.getName());
            listOfBooks.remove(listNumberIndex);
            return;
        } else if (quantityToDelete > currentNumberInInventory) {
            Ui.printDeletingTooManyBooksMessage(currentNumberInInventory, quantityToDelete, inputBook.getName());
            throw new TooLargeQuantityException();
        }
        inputBook.setNumberInInventory(newNumberInInventory);
        inputBook.setNumberTotal(newNumberTotal);
        Ui.printDeleteStatement(quantityToDelete, inputBook.getName(), newNumberInInventory, newNumberTotal);

        if (newNumberTotal <= 0) {
            System.out.println(inputBook.getName() + " has been deleted from the library's list!");
            this.listOfBooks.remove(listNumberIndex);
        }
    }

    /**
     * Parses and validates the index input for the book to delete.
     *
     * @return The validated index.
     * @throws IndexOutOfListBounds If the provided index is outside the range of the book list.
     */
    public int setInputtedIndex() throws IndexOutOfListBounds {
        int index = Integer.parseInt(this.splitQuantity[0].trim());
        if (index <= 0 || inputtedIndex > listOfBooks.size()) {
            throw new IndexOutOfListBounds();
        }
        return index;
    }
}
