package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.NegativeQuantityException;
import bookmarked.exceptions.MaxIntNumberException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.WrongQuantityException;
import bookmarked.exceptions.InvalidStringException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

public class AddCommand extends Command {
    private static final int DEFAULT_QUANTITY = 1;
    private static final int MAX_QUANTITY = 1000;
    private static final String MAX_QUANTITY_STRING = "1000";
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private String[] splitQuantity;
    private File bookDataFile;
    private int quantityToAdd;
    private boolean hasQuantityArgument;

    /**
     * AddCommand handles the addition of books when add command is called
     *
     * @param newItem      the item to be added into the list
     * @param listOfBooks  the current list of books in the library
     * @param bookDataFile to store the books
     */
    public AddCommand(String newItem, ArrayList<Book> listOfBooks, File bookDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.hasQuantityArgument = false;
    }

    /**
     * handles the command by user. Adds item into list and catches for empty arguments
     */

    @Override
    public void handleCommand() {
        assert newItem != null : "Item should not be null";
        String[] newSplitBook = this.newItem.split("add");
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
     * processAddCommand adds book to the list if not empty
     *
     * @throws EmptyArgumentsException throws if there is no description
     */
    public void processAddCommand(String[] newSplitBook) throws EmptyArgumentsException {
        // checks if splitQuantity contains only the word "add" or if there are only white spaces after it
        if (newSplitBook.length < 1) {
            throw new EmptyArgumentsException();
        }

        this.splitQuantity = newSplitBook[1].split(" /quantity ");
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

    public void runAddCommand() throws MaxIntNumberException, InvalidStringException {
        String bookTitle = splitQuantity[0].trim();
        Book inputBook = getExistingBook(bookTitle);

        try {
            checkTitleValidity(bookTitle);
        } catch (InvalidStringException e) {
            throw new InvalidStringException();
        }

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


    public void checkTitleValidity(String bookName) throws InvalidStringException {
        if (bookName.matches("^[0-9]+$") || bookName.contains("|")) {
            throw new InvalidStringException();
        }
    }


    public Book getExistingBook(String title) {
        for (Book currentBook : this.listOfBooks) {
            if (currentBook.getName().matches(title)) {
                return currentBook;
            }
        }
        return null;
    }
}
