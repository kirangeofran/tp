package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

public class AddCommand extends Command {
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private String[] splitItem;
    private File bookDataFile;

    /**
     * AddCommand handles the addition of books when add command is called
     * @param newItem the item to be added into the list
     * @param listOfBooks the current list of books in the library
     * @param splitItem item split into 2 arrays, command and description
     * @param bookDataFile to store the books
     */
    public AddCommand(String newItem, ArrayList<Book> listOfBooks, String[] splitItem, File bookDataFile){
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.splitItem = splitItem;
        this.bookDataFile = bookDataFile;
    }

    /**
     * handles the command by user. Adds item into list and catches for empty arguments
     */

    @Override
    public void handleCommand() {
        assert newItem != null : "Item should not be null";
        String[] newSplitBook = this.newItem.split("add");
        try {
            processAddCommand(newSplitBook, listOfBooks);
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
     * @param listOfBooks the list of books in the library
     * @throws EmptyArgumentsException throws if there is no description
     */

    public void processAddCommand(String[] newSplitBook, ArrayList<Book> listOfBooks)
            throws EmptyArgumentsException {
        // checks if newSplitBook contains only the word "add" or if there are only white spaces after it
        if (newSplitBook.length < 1 || newSplitBook[1].isBlank()) {
            throw new EmptyArgumentsException();
        }
        Book bookName = new Book(newSplitBook[1].trim());
        this.listOfBooks.add(bookName);
        System.out.println("Added " + bookName.getName() + "!");
    }
}
