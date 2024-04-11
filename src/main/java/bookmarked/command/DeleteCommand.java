package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.IndexOutOfListBounds;
import bookmarked.exceptions.WrongFormatQuantityException;
import bookmarked.exceptions.NegativeQuantityException;
import bookmarked.exceptions.MaxIntNumberException;
import bookmarked.exceptions.TooLargeQuantityException;
import bookmarked.exceptions.WrongQuantityException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

public class DeleteCommand extends Command {
    private static final int DEFAULT_QUANTITY = 1;
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;
    private String[] splitQuantity;
    private int quantityToDelete;
    private boolean hasQuantityArgument;
    private int inputtedIndex;


    public DeleteCommand(String newItem, ArrayList<Book> listOfBooks, File bookDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.hasQuantityArgument = false;
    }

    /**
     * handles the delete command
     * iterates through the list of books to find the book corresponding to index number
     */
    @Override
    public void handleCommand() {
        String[] newSplitBook = this.newItem.split("delete");

        try {
            processDeleteCommand(newSplitBook);
            assert !this.listOfBooks.isEmpty() : "The current list of books should not be empty";
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
     * deletes the book based on index number keyed
     *
     * @throws EmptyListException      if the list of books is empty
     * @throws EmptyArgumentsException if there is no description after command
     * @throws IndexOutOfListBounds    if the index is less or more than the number of books
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

        this.splitQuantity = newSplitBook[1].split(" /quantity");
        if (this.splitQuantity[0].isBlank()) {
            throw new EmptyArgumentsException();
        }

        this.splitQuantity = newSplitBook[1].split(" /quantity");

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
            } else if (quantityToDelete > 1000) {
                throw new MaxIntNumberException();
            }
            return quantityToDelete;
        } else {
            return DEFAULT_QUANTITY;
        }

    }


    public void checkQuantityStringValidity() throws WrongQuantityException, MaxIntNumberException {
        if (splitQuantity.length < 2 || splitQuantity[1].isBlank()) {
            throw new WrongQuantityException();
        }

        String quantityString = splitQuantity[1].trim();
        if (quantityString.length() >= 4 && !quantityString.equals("1000")) {
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
        int newNumberInInventory = currentNumberInInventory - quantityToDelete;
        int newNumberTotal = inputBook.getNumberTotal() - quantityToDelete;

        if (quantityToDelete > currentNumberInInventory) {
            Ui.printDeletingTooManyBooksMessage(currentNumberInInventory, quantityToDelete, inputBook.getName());
            throw new TooLargeQuantityException();
        }
        inputBook.setNumberInInventory(newNumberInInventory);
        inputBook.setNumberTotal(newNumberTotal);
        System.out.println("Deleted " + quantityToDelete + " copies of " + inputBook.getName() + "!");
        System.out.println("There are now " + newNumberInInventory + " copies in the library's inventory, and "
                + newNumberTotal + " copies in total.");

        if (newNumberTotal <= 0) {
            System.out.println(inputBook.getName() + " has been deleted from the library's list!");
            this.listOfBooks.remove(listNumberIndex);
        }
    }


    public int setInputtedIndex() throws IndexOutOfListBounds {
        int index = Integer.parseInt(this.splitQuantity[0].trim());
        if (index <= 0 || inputtedIndex > listOfBooks.size()) {
            throw new IndexOutOfListBounds();
        }
        return index;
    }

}