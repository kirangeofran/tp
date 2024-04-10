package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.BookMarkedException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.WrongAddQuantityException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

public class AddCommand extends Command {
    private static final int DEFAULT_QUANTITY = 1;
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private String[] splitQuantity;
    private String[] splitItem;
    private File bookDataFile;
    private int quantityToAdd;
    private boolean hasQuantityArgument;

    /**
     * AddCommand handles the addition of books when add command is called
     * @param newItem the item to be added into the list
     * @param listOfBooks the current list of books in the library
     * @param splitItem user input split int an array through whitespaces
     * @param bookDataFile to store the books
     */
    public AddCommand(String newItem, ArrayList<Book> listOfBooks, String[] splitItem, File bookDataFile){
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.splitItem = splitItem;
    }

    /**
     * handles the command by user. Adds item into list and catches for empty arguments
     */

    @Override
    public void handleCommand() {
        assert newItem != null : "Item should not be null";
        String[] newSplitBook = this.newItem.split("add");
        this.splitQuantity = newSplitBook[1].split(" /quantity");

        try {
            processAddCommand(newSplitBook);
            assert newSplitBook.length >= 1 : "There should be an argument to the command";
            assert !this.listOfBooks.isEmpty() : "The current list of books should not be empty";
            BookStorage.writeBookToTxt(this.bookDataFile, listOfBooks);
        } catch (EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
        }
    }

    /**
     * processAddCommand adds book to the list if not empty
     * @param newSplitBook the command split by the word 'add' giving the command and description in different arrays
     * @throws EmptyArgumentsException throws if there is no description
     */
    public void processAddCommand(String[] newSplitBook)
            throws EmptyArgumentsException {
        // checks if splitQuantity contains only the word "add" or if there are only white spaces after it
        if (this.splitQuantity.length < 1 || this.splitQuantity[0].isBlank()) {
            throw new EmptyArgumentsException();
        }

        try {
            setQuantityToAdd();
            runAddCommand();
        } catch (BookMarkedException e) {
            quantityToAdd = DEFAULT_QUANTITY;
            hasQuantityArgument = false;
            runAddCommand();
        } catch (WrongAddQuantityException e) {
            Ui.printBlankAddQuantity();
        } catch (NumberFormatException e) {
            Ui.printWrongAddQuantityFormat();
        }

    }


    public void setQuantityToAdd() throws BookMarkedException, WrongAddQuantityException, NumberFormatException {
        //if there is no /quantity argument
        if (newItem.contains(" /quantity")) {
            hasQuantityArgument = true;
        }

        if (!hasQuantityArgument) {
            throw new BookMarkedException();
        } else if (splitQuantity.length < 2 || splitQuantity[1].isBlank()) {  //guaranteed that hasQuantityArgument is true
            throw new WrongAddQuantityException();
        }

        quantityToAdd = Integer.parseInt(splitQuantity[1].trim());
    }


    public void runAddCommand() {
        String bookTitle = splitQuantity[0].trim();
        Book inputBook = getExistingBook(bookTitle);

        //if the current book is a new book
        if (inputBook == null) {
            Book bookName = new Book(bookTitle);
            this.listOfBooks.add(bookName);
            bookName.setNumberInInventory(quantityToAdd);
            bookName.setNumberTotal(quantityToAdd);
            System.out.println("Added " + bookName.getName() + "!");
        } else {    //if the current book already exists in the library
            int currentNumberInInventory = inputBook.getNumberInInventory();
            int currentNumberTotal = inputBook.getNumberTotal();
            inputBook.setNumberInInventory(currentNumberInInventory + quantityToAdd);
            inputBook.setNumberTotal(currentNumberTotal + quantityToAdd);
            System.out.println("Added " + quantityToAdd + " copies of " + inputBook.getName() + "!");
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
