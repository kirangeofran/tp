package bookmarked.command;

import bookmarked.Book;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.BookNotBorrowedException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The {@code ExtendCommand} class handles the extension of the borrowing period for a book.
 */
public class ExtendCommand extends Command {
    private String[] commandParts;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;

    /**
     * Constructs an {ExtendCommand} with the specified command parts, list of books, and file.
     *
     * @param commandParts the parts of the command input, including the book name
     * @param listOfBooks  the current list of books
     * @param bookDataFile the file where the list of books is stored
     */
    public ExtendCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile) {
        this.commandParts = commandParts;
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
    }

    /**
     * Executes the extend command by trying to extend the due date of a borrowed book.
     * It catches specific exceptions and delegates error messages to the {@code Ui} class.
     */
    @Override
    public void handleCommand() {
        try {
            runExtendCommand();
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (BookNotFoundException e) {
            Ui.printBookNotFoundExceptionMessage(); // Call the method without parameters
        } catch (BookNotBorrowedException e) {
            Ui.printBookNotBorrowedExceptionMessage(); // Call the method without parameters
        }
    }

    /**
     * Runs the logic to extend the due date of a borrowed book. If the book is not found
     * or is not currently borrowed, it throws corresponding exceptions.
     *
     * @throws BookNotFoundException      if the book name provided does not match any book
     * @throws BookNotBorrowedException   if the book found is not currently borrowed
     */
    private void runExtendCommand() throws BookNotFoundException, BookNotBorrowedException {
        if (commandParts.length <= 1) {
            throw new BookNotFoundException();
        }

        String bookNameToExtend = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length));
        boolean bookFound = false;

        for (Book book : listOfBooks) {
            if (book.getName().equalsIgnoreCase(bookNameToExtend)) {
                bookFound = true;
                if (book.getIsBorrowed()) {
                    book.extendDueDate(); // Make sure this method exists in your Book class
                    System.out.println("Due date for '" + bookNameToExtend + "' has been extended by a week.");
                    return;
                } else {
                    throw new BookNotBorrowedException();
                }
            }
        }
        if (!bookFound) {
            throw new BookNotFoundException();
        }
    }

}
