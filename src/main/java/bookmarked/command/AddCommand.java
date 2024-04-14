package bookmarked.command;

import bookmarked.Book;
import bookmarked.arguments.TitleValidity;
import bookmarked.exceptions.NegativeQuantityException;
import bookmarked.exceptions.MaxIntNumberException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.WrongQuantityException;
import bookmarked.exceptions.InvalidStringException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;


/**
 * Represents a command to add books to the library system.
 * This class is responsible for handling the 'add' command input by the user.
 */
public class AddCommand extends Command {
    private static final int DEFAULT_QUANTITY = 1;
    private static final int MAX_QUANTITY = 1000;
    private static final String MAX_QUANTITY_STRING = "1000";
    private static final String COMMAND_STRING = "add";
    private static final String ARGUMENT_STRING = " /quantity ";
    private final String newItem;
    private final File bookDataFile;
    private ArrayList<Book> listOfBooks;
    private String[] splitQuantity;
    private int quantityToAdd;
    private boolean hasQuantityArgument;


    /**
     * Constructs an AddCommand object with the specified item, book list, and file storage.
     *
     * @param newItem      The title of the new item to be added.
     * @param listOfBooks  The current list of books maintained in the library.
     * @param bookDataFile The file used for data storage of book information.
     */
    public AddCommand(String newItem, ArrayList<Book> listOfBooks, File bookDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.hasQuantityArgument = false;
    }

    /**
     * Executes the add command, adding a new item to the book list.
     * It parses the command for quantity and handles any possible exceptions related to the add operation.
     */

    @Override
    public void handleCommand() {
        assert newItem != null : "Item should not be null";
        String[] newSplitBook = this.newItem.split(COMMAND_STRING);
        if (newSplitBook.length < 1) {
            Ui.printEmptyArgumentsMessage();
            return;
        }

        try {
            processAddCommand(newSplitBook);
            assert !this.listOfBooks.isEmpty() : "The current list of books should not be empty";
            BookStorage.writeBookToTxt(this.bookDataFile, listOfBooks);
        } catch (EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
        }
    }

    /**
     * Processes the addition of a book to the library, based on the parsed command arguments.
     *
     * @throws EmptyArgumentsException if the command arguments are empty or invalid.
     */
    public void processAddCommand(String[] newSplitBook) throws EmptyArgumentsException {
        // checks if splitQuantity contains only the word "add" or if there are only white spaces after it
        if (newSplitBook.length < 1) {
            throw new EmptyArgumentsException();
        }

        this.splitQuantity = newSplitBook[1].split(ARGUMENT_STRING);
        if (this.splitQuantity[0].isBlank()) {
            throw new EmptyArgumentsException();
        }

        try {
            quantityToAdd = setQuantityToAdd();
            runAddCommand();
        } catch (WrongQuantityException e) {
            Ui.printBlankAddQuantity();
        } catch (NumberFormatException e) {
            Ui.printWrongQuantityFormat();
        } catch (MaxIntNumberException e) {
            Ui.printMaxNumberMessage();
        } catch (NegativeQuantityException e) {
            Ui.printNegativeAddQuantityMessage();
        } catch (InvalidStringException e) {
            Ui.printInvalidTitleMessage();
        }
    }

    /**
     * Determines the quantity of the book to be added.
     * The quantity is parsed from the command arguments or set to a default value.
     *
     * @return The quantity to be added.
     * @throws WrongQuantityException    if the quantity argument is missing or invalid.
     * @throws NumberFormatException     if the quantity is not a valid number format.
     * @throws MaxIntNumberException     if the specified quantity exceeds the maximum allowed.
     * @throws NegativeQuantityException if the specified quantity is negative.
     */
    public int setQuantityToAdd() throws WrongQuantityException, NumberFormatException,
            MaxIntNumberException, NegativeQuantityException {
        // if there is no /quantity argument
        if (newItem.contains(" /quantity")) {
            hasQuantityArgument = true;
        }

        if (hasQuantityArgument) {
            checkQuantityStringValidity();
            int quantityToAdd = Integer.parseInt(splitQuantity[1].trim());
            if (quantityToAdd <= 0) {
                throw new NegativeQuantityException();
            } else if (quantityToAdd > MAX_QUANTITY) {
                throw new MaxIntNumberException();
            }
            return quantityToAdd;
        } else {
            return DEFAULT_QUANTITY;
        }

    }

    /**
     * Validates the format of the quantity string argument.
     *
     * @throws WrongQuantityException if the quantity string is invalid or missing.
     * @throws MaxIntNumberException  if the quantity exceeds the maximum limit.
     */
    public void checkQuantityStringValidity() throws WrongQuantityException, MaxIntNumberException {
        if (splitQuantity.length < 2 || splitQuantity[1].isBlank()) {
            throw new WrongQuantityException();
        }

        String quantityString = splitQuantity[1].trim();
        if (quantityString.length() >= 4 && !quantityString.equals(MAX_QUANTITY_STRING)) {
            // Checks if the input itself is longer than a 4 digit number, and if it's not, checks if it's any
            // other 4 digit number than 1000, as 1000 is the maximum number of copies.
            if (quantityString.matches("^[0-9]+$")) {
                //Check if the input string contains only numbers
                throw new MaxIntNumberException();
            } else {
                // Contains other symbols such as letters or special characters
                throw new NumberFormatException();
            }
        }
    }

    /**
     * Executes the addition of books to the library, updating inventory numbers accordingly.
     *
     * @throws MaxIntNumberException  if adding the specified quantity would exceed the library's limits.
     * @throws InvalidStringException if the book title is invalid.
     */
    public void runAddCommand() throws MaxIntNumberException, InvalidStringException {
        String bookTitle = splitQuantity[0].trim();

        try {
            TitleValidity.checkTitleValidity(bookTitle);
        } catch (InvalidStringException e) {
            throw new InvalidStringException();
        }

        Book inputBook = getExistingBook(bookTitle);
        // if the current book is a new book
        if (inputBook == null) {
            Book bookName = new Book(bookTitle);
            this.listOfBooks.add(bookName);
            bookName.setNumberInInventory(quantityToAdd);
            bookName.setNumberTotal(quantityToAdd);
            System.out.println("Added " + bookName.getName() + "!");
        } else {    // if the current book already exists in the library
            int newNumberInInventory = inputBook.getNumberInInventory() + quantityToAdd;
            int newNumberTotal = inputBook.getNumberTotal() + quantityToAdd;

            if (newNumberInInventory > MAX_QUANTITY || newNumberTotal > MAX_QUANTITY) {
                throw new MaxIntNumberException();
            }

            inputBook.setNumberInInventory(newNumberInInventory);
            inputBook.setNumberTotal(newNumberTotal);
            System.out.println("Added " + quantityToAdd + " copies of " + inputBook.getName() + "!");
        }
    }


    /**
     * Retrieves an existing book from the library's inventory based on the given title.
     *
     * @param title The title of the book to be retrieved.
     * @return The Book object if found, or null if no matching book is found.
     */
    public Book getExistingBook(String title) {
        for (Book currentBook : this.listOfBooks) {
            if (currentBook.getName().matches(title)) {
                return currentBook;
            }
        }
        return null;
    }
}
