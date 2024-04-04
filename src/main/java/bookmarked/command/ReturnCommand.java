package bookmarked.command;

import bookmarked.Book;
import bookmarked.User;
import bookmarked.exceptions.EmptyListException;
import bookmarked.storage.BookStorage;
import bookmarked.storage.UserStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the "return" command from the user.
 * When executed, it returns the book with the given name from the list of books,
 * marking it as not borrowed. Assumes that the book name is passed in the commandParts array.
 */
public class ReturnCommand extends Command {
    private String bookName;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;

    /**
     * Constructs a ReturnCommand object.
     *
     * @param commandParts An array of strings, where the first element is the command,
     *                     and the subsequent elements are the parts of the book name.
     * @param listOfBooks  The list of books from which a book will be returned.
     * @param bookDataFile The data file where books are stored.
     */
    public ReturnCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, File userDataFile) {
        assert listOfBooks != null : "list of books should not be empty";
        assert commandParts != null : "commandParts should not be null";
        assert commandParts.length > 1 : "commandParts should contain at least the command and the book name";
        this.bookName =  String.join(" ", List.of(commandParts).subList(1, commandParts.length));
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
    }

    /**
     * Executes the "return" command.
     * Filters the list of books for any books that match the given book name and marks them as returned.
     * Updates the book data file with the changes.
     */
    @Override
    public void handleCommand() {
        // Filter the list for books that match the name
        List<Book> foundBooks = listOfBooks.stream()
                .filter(book -> book.getName().equalsIgnoreCase(bookName))
                .collect(Collectors.toList());
        //assert !foundBooks.isEmpty() : "Book should exist to return";

        try {
            runReturnCommand(foundBooks);
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }


    /**
     * Marks the books found in the list with the name provided as returned.
     * If the book is not found, prints an error message.
     *
     * @param foundBooks The list of books with names that match the book to be returned.
     * @throws EmptyListException If the list of books is empty.
     */
    public void runReturnCommand(List<Book> foundBooks) throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        if (foundBooks.isEmpty()) {
            System.out.println("Book not found: " + bookName);
            return;
        }

        Book returnedBook = null;
        for (Book currentBook : foundBooks) {
            returnedBook = currentBook;
            if (currentBook.getIsBorrowed()) {
                currentBook.setReturned();

                System.out.println("Returned " + currentBook.getName() + "!");
            } else {
                System.out.println("Book is not borrowed: " + currentBook.getName());
            }
        }

        Iterator<User> iterator = listOfUsers.iterator();
        while (iterator.hasNext()) {
            User currentUser = iterator.next();
            currentUser.unborrowBook(returnedBook);
            if (currentUser.getUserBooks().isEmpty()) {
                iterator.remove();
            }
        }
        UserStorage.writeUserToTxt(userDataFile, listOfUsers);
    }
}


