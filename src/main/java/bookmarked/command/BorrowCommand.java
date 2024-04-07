package bookmarked.command;

import bookmarked.Book;
import bookmarked.User;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.WrongInputFormatException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;
import bookmarked.storage.UserStorage;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the "borrow" command from the user.
 * When executed, it borrows the book with the given name from the list of books,
 * if the book is available. It also sets the due date based on a default borrow period.
 */
public class BorrowCommand extends Command {

    private static final Period DEFAULT_BORROW_PERIOD = Period.ofWeeks(2);
    private String bookName;
    private String userName;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;

    /**
     * Constructs a BorrowCommand object with the specified parameters.
     *
     * @param commandParts The array of strings containing the command and its arguments,
     *                     where the command is the first element and the book name
     *                     is constructed from the subsequent elements.
     * @param listOfBooks  The list of books to be searched for borrowing.
     * @param bookDataFile The file where book data is stored.
     */
    public BorrowCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, String newItem, File userDataFile)
            throws EmptyArgumentsException, WrongInputFormatException {
        assert commandParts != null : "commandParts should not be null";
        assert commandParts.length > 1 : "commandParts should contain at least two elements";

        String itemUserName = newItem.substring(7);
        String[] splitParts = itemUserName.split ("/by");
        assert splitParts.length > 1: "please enter both the borrowed book and userName";

        if (!containsUser (commandParts)) {
            throw new EmptyArgumentsException();
        }

        if (isMoreThanOneBy(splitParts)) {
            throw new WrongInputFormatException();
        }

        this.bookName = splitParts[0].trim();
        this.userName = splitParts[1].trim();
        assert listOfBooks != null : "listOfBooks should not be null";
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
    }

    private static boolean isMoreThanOneBy(String[] splitParts) {
        return splitParts.length > 2;
    }

    public boolean containsUser(String[] commandParts) {
        for (int i = 0; i <commandParts.length;i ++) {
            if (commandParts[i].equalsIgnoreCase("/by")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Executes the "borrow" command.
     * Searches for the book by name in the list of books and attempts to borrow it.
     * Updates the book data file with the changes if the book is successfully borrowed.
     */
    @Override
    public void handleCommand() {
        // Find the book with the matching name
        List<Book> foundBooks = listOfBooks.stream()
                .filter(book -> book.getName().equalsIgnoreCase(bookName))
                .collect(Collectors.toList());
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
     * Attempts to borrow a book from the list based on its availability.
     * If the book is found and available, marks it as borrowed and sets the return date.
     * If the book is not available, informs the user of the expected return date.
     * If the book is not found, notifies the user that the book could not be found.
     *
     * @param foundBooks The list of books with names matching the one to borrow.
     * @throws EmptyListException If the list of books is empty.
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
                System.out.println("Borrowed " + bookToBorrow.getName() + " by " + userName +"!");
                System.out.println("Please return by " + bookToBorrow.getFormattedReturnDate() + ".");
            } else {
                System.out.println("Book is currently unavailable. Expected return date is " +
                        bookToBorrow.getFormattedReturnDate() + ".");
            }
        } else {
            System.out.println("Book not found: " + bookName);
        }
    }
    private void updateListOfUsers(Book book, String userName) {
        for (User user : listOfUsers) {
            if (user.getName().equalsIgnoreCase(userName)) {
                user.borrowedBook(book); // Add the borrowed book to the user's list of borrowed books
                UserStorage.writeUserToTxt(userDataFile, listOfUsers);
                return;
            }
        }
        // If user not found, create a new user and add the borrowed book
        User newUser = new User(userName);
        newUser.borrowedBook(book);
        listOfUsers.add(newUser);
        UserStorage.writeUserToTxt(userDataFile, listOfUsers);
    }
}
