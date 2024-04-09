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
import java.util.Arrays ;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the "return" command from the user.
 * When executed, it allows the user to return a book specified by either its name or index in the list of books,
 * marking it as not borrowed. The command distinguishes between a numeric input, treated as an index,
 * and non-numeric input, treated as a book name. This flexibility allows users to easily manage book returns
 * by specifying the most convenient identifier for them at the moment.
 * Assumes that if a numeric value is provided in the commandParts array,
 * it represents the index of the book to be returned, adjusted for a zero-based index.
 * If a non-numeric value is provided, it is treated as the name of the book to be returned.
 * This approach simplifies the user interaction with the system, making the book returning process more intuitive.
 */
public class ReturnCommand extends Command {
    private String bookName = null ;
    private int bookIndex = -1; // Index starting from 0
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;

    /**
     * Constructs a ReturnCommand object with the specified parameters.
     * The constructor attempts to parse the second element of the commandParts array as an index.
     * If parsing fails (indicating the input is not numeric), it concatenates the remaining elements
     * as the book name. This allows users to return books by specifying either their index in the list
     * or their name.
     *
     * @param commandParts An array of strings, where the first element is the command,
     *                     the second element is either the index or the first part of the book name,
     *                     and any subsequent elements are the remaining parts of the book name if applicable.
     * @param listOfBooks  The list of books from which a book will be returned.
     * @param bookDataFile The data file where books are stored.
     */
    public ReturnCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, File userDataFile) {
        assert listOfBooks != null : "list of books should not be empty";
        assert commandParts != null : "commandParts should not be null";
        assert commandParts.length > 1 : "commandParts should contain at least the command and the book name";
        String commandInput = Arrays.stream(commandParts)
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .collect(Collectors.joining(" "));

        // Now, attempt to parse the book index from the processed command input.
        String[] splitParts = commandInput.split("\\s+", 2); // Split into two parts at most.

        // The book index or name is expected to be after the command 'return'
        String bookIdentifier = splitParts.length > 1 ? splitParts[1] : "";

        try {
            this.bookIndex = Integer.parseInt(bookIdentifier) - 1; // Use the processed identifier to parse the index
        } catch (NumberFormatException e) {
            this.bookName = bookIdentifier; // If not a number, treat as a book name
        }

        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
    }

    /**
     * Executes the "return" command by searching for the specified book in the list of books,
     * either by its index or by its name, and marks the found book(s) as not borrowed.
     * Updates the book data file with the changes. If the specified book is not found,
     * or if the specified index is out of bounds, it prints an error message.
     */
    @Override
    public void handleCommand() {
        // Filter the list for books that match the name
        List<Book> foundBooks ;
        if (bookIndex >= 0 && bookIndex < listOfBooks.size()) {
            foundBooks = List.of(listOfBooks.get(bookIndex));
        } else {
            foundBooks = listOfBooks.stream()
                    .filter(book -> book.getName().equalsIgnoreCase(bookName))
                    .collect(Collectors.toList());
        }

        try {
            runReturnCommand(foundBooks);
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }

    /**
     * Marks the books found in the list as returned. This method handles the case where multiple
     * copies of a book exist by marking all matched copies as returned. It also updates the status
     * of the book in the system and notifies the user of the action taken. If no books match
     * the provided identifier (name or index), it informs the user that the book was not found.
     *
     * @param foundBooks The list of books that match the specified identifier for return.
     * @throws EmptyListException If the overall list of books is empty, indicating a system error.
     */
    public void runReturnCommand(List<Book> foundBooks) throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        if (foundBooks.isEmpty()) {
            System.out.println("Book not found: " + bookName);
            return;
        }

        for (Book currentBook : foundBooks) {
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


