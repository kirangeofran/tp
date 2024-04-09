package bookmarked.command;

import bookmarked.Book;
import bookmarked.User;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.WrongInputFormatException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the "borrow" command from the user, allowing for borrowing a book specified either by its name
 * or by its index in the list of books. This command enhances flexibility by enabling users to borrow books
 * using the most convenient identifier for them at the moment. If a book is available for borrowing, the command
 * marks the book as borrowed and sets a due date based on a default borrow period.
 * <p>
 * This command assumes that if a numeric value is provided as part of the command, it represents the index
 * of the book to be borrowed, adjusted for a zero-based index. If a non-numeric value is provided, it is treated
 * as the name of the book to be borrowed. The distinction between a book name and index input allows for a more
 * intuitive interaction with the borrowing system.
 */
public class BorrowCommand extends Command {

    private static final Period DEFAULT_BORROW_PERIOD = Period.ofWeeks(2);
    private String bookName;

    private int bookIndex = -1;
    private String userName;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;

    /**
     * Constructs a BorrowCommand object with specified parameters, allowing the user to specify
     * the book to be borrowed either by name or by index. The constructor parses the input to determine
     * whether the user has provided a numeric index or a book name.
     *
     * @param commandParts An array of strings, where the first element is the command,
     *                     the second element is either the index or the first part of the book name,
     *                     and any subsequent elements are the remaining parts of the book name if applicable.
     * @param listOfBooks  The list of books available for borrowing.
     * @param bookDataFile The file where book data is stored.
     * @param listOfUsers  The list of users in the system.
     * @param newItem      A string representing the new item to be added, including the book name/index and the user.
     * @throws EmptyArgumentsException   If the required arguments are not provided.
     * @throws WrongInputFormatException If the input format does not match the expected format.
     */
    public BorrowCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, String newItem)
            throws EmptyArgumentsException, WrongInputFormatException {
        assert commandParts != null : "commandParts should not be null";
        assert commandParts.length > 1 : "commandParts should contain at least two elements";

        String itemUserName = newItem.substring(7);
        String[] splitParts = itemUserName.split("/by");
        assert splitParts.length > 1 : "please enter both the borrowed book and userName";

        if (!containsUser(commandParts)) {
            throw new EmptyArgumentsException();
        }

        if (isMoreThanOneBy(splitParts)) {
            throw new WrongInputFormatException();
        }

        try {
            this.bookIndex = Integer.parseInt(splitParts[0].trim()) - 1;
        } catch (NumberFormatException e) {
            this.bookName = splitParts[0].trim();
        }


        this.userName = splitParts[1].trim();
        assert listOfBooks != null : "listOfBooks should not be null";
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
    }

    private static boolean isMoreThanOneBy(String[] splitParts) {
        return splitParts.length > 2;
    }

    public boolean containsUser(String[] commandParts) {
        for (int i = 0; i < commandParts.length; i++) {
            if (commandParts[i].equalsIgnoreCase("/by")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes the "borrow" command by searching for the specified book in the list of books,
     * either by its index or by its name, and attempts to mark the book as borrowed if it is available.
     * Updates the book data file with the changes. If the specified book is not available or not found,
     * it prints an appropriate message to the user.
     */
    @Override
    public void handleCommand() {
        // Find the book with the matching name
        List<Book> foundBooks = new ArrayList<>();
        if (bookIndex >= 0 && bookIndex < listOfBooks.size()) {
            foundBooks.add(listOfBooks.get(bookIndex));
        } else {
            foundBooks = listOfBooks.stream()
                    .filter(book -> book.getName().equalsIgnoreCase(bookName))
                    .collect(Collectors.toList());
        }
        try {
            runBorrowCommand(foundBooks);
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        } catch (WrongInputFormatException e) {
            Ui.printWrongInputFormat();
        }
    }

    /**
     * Attempts to mark a book as borrowed based on its availability. If the book is available,
     * it is marked as borrowed, and a due date is set. If the book is currently borrowed, the user is
     * informed of the expected return date. This method updates the user's list of borrowed books accordingly.
     *
     * @param foundBooks The list of books that match the specified identifier for borrowing.
     * @throws EmptyListException        If the overall list of books is empty.
     * @throws WrongInputFormatException If the input format is incorrect.
     */
    public void runBorrowCommand(List<Book> foundBooks)
            throws EmptyListException, WrongInputFormatException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        if (!foundBooks.isEmpty()) {
            Book bookToBorrow = foundBooks.get(0);
            if (bookToBorrow.isAvailable()) {
                bookToBorrow.borrowBook(LocalDate.now(), DEFAULT_BORROW_PERIOD);
                updateListOfUsers(bookToBorrow, userName);
                System.out.println("Borrowed " + bookToBorrow.getName() + " by " + userName + "!");
                System.out.println("Please return by " + bookToBorrow.getFormattedReturnDate() + ".");
            } else {
                System.out.println("Book is currently unavailable. Expected return date is " +
                        bookToBorrow.getFormattedReturnDate() + ".");
            }
        } else {
            System.out.println("Book not found: " + bookName);
        }
    }

    /**
     * Updates the list of users with the information about the borrowed book. If the user does not exist in the
     * list, a new user is created and added to the list.
     *
     * @param book     The book that has been borrowed.
     * @param userName The name of the user who borrowed the book.
     */
    private void updateListOfUsers(Book book, String userName) {
        for (User user : listOfUsers) {
            if (user.getName().equalsIgnoreCase(userName)) {
                user.borrowedBook(book); // Add the borrowed book to the user's list of borrowed books
                return;
            }
        }
        // If user not found, create a new user and add the borrowed book
        User newUser = new User(userName);
        newUser.borrowedBook(book);
        listOfUsers.add(newUser);
    }
}

