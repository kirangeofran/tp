package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
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

    public BorrowCommand(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, String newItem, File userDataFile)
            throws EmptyArgumentsException, WrongInputFormatException {
        setArguments(commandParts, listOfBooks, bookDataFile, listOfUsers, newItem, userDataFile);
    }

    private void setArguments(String[] commandParts, ArrayList<Book> listOfBooks, File bookDataFile,
                              ArrayList<User> listOfUsers, String newItem, File userDataFile)
            throws EmptyArgumentsException, WrongInputFormatException {
        if (commandParts == null || commandParts.length <= 1) {
            throw new EmptyArgumentsException();
        }
        if (newItem == null) {
            throw new EmptyArgumentsException();
        }

        if (!containsUser(commandParts)) {
            throw new EmptyArgumentsException();
        }

        // Assuming the newItem string starts right after the command keyword with the book name/index.
        String itemUserName = newItem.substring(newItem.indexOf(' ') + 1);
        String[] splitParts = itemUserName.split(" /by ");
        if (splitParts.length < 2 || splitParts[1].trim().isEmpty()) {
            throw new EmptyArgumentsException();
        }

        try {
            this.bookIndex = Integer.parseInt(splitParts[0].trim()) - 1;
        } catch (NumberFormatException e) {
            this.bookName = splitParts[0].trim();  // Assume non-numeric is a book name.
        }

        if (isMoreThanOneBy(splitParts)) {
            throw new WrongInputFormatException();
        }

        this.userName = splitParts[1].trim();
        if (this.userName.isEmpty()) {
            throw new EmptyArgumentsException();
        }

        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
    }


    @Override
    public void handleCommand() {
        List<Book> foundBooks;
        if (bookIndex >= 0 && bookIndex < listOfBooks.size()) {
            foundBooks = new ArrayList<>();
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
        }  catch (WrongInputFormatException e) {
            Ui.printWrongInputFormat();
        }
    }


    private static boolean isMoreThanOneBy(String[] splitParts) {
        return splitParts.length > 2;
    }


    /**
     * checks whether there is a user input
     *
     * @param commandParts the command split into n arrays based on how many spaces it contains
     * @return true if one of the arrays contain by, false otherwise
     */
    public boolean containsUser(String[] commandParts) {
        for (String commandPart : commandParts) {
            if (commandPart.equalsIgnoreCase("/by")) {
                return true;
            }
        }
        return false;
    }



    private void updateBookIndex() {
        for (int i = 0; i < listOfBooks.size(); i += 1) {
            String currentBookName = listOfBooks.get(i).getName();
            if (currentBookName.equals(bookName)) {
                this.bookIndex = i;
            }
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
            if (bookToBorrow.isAvailable() && !isBookBorrowed(userName, bookToBorrow)) {
                bookToBorrow.borrowBook(LocalDate.now(), DEFAULT_BORROW_PERIOD);
                updateListOfUsers(userName, LocalDate.now());
                System.out.println("Borrowed " + bookToBorrow.getName() + " by " + userName + "!");
                System.out.println("Please return by " + bookToBorrow.getFormattedReturnDate() + ".");
            } else if (isBookBorrowed(userName, bookToBorrow)) {
                System.out.println(userName + " has already borrowed this book. Please return before borrowing it again.");
            } else {
                System.out.println("There are currently no available copies of the book in the inventory.");
            }
        } else {
            System.out.println("Book not found: " + bookName);
        }
    }


    /**
     * updates the list of users by adding the new user and its borrowed books to the user's list
     * Checks whether the user has already borrowed books and hence is already in the user list
     * If user is in user list, add new book to user's current list
     * If user is a new user, create the new user and add the new book into user list
     *
     * @param userName the name of the user
     */
    private void updateListOfUsers(String userName, LocalDate borrowDate) {
        LocalDate returnDueDate = borrowDate.plus(DEFAULT_BORROW_PERIOD);
        for (User user : listOfUsers) {
            if (user.getName().equalsIgnoreCase(userName)) {
                user.setListOfBooks(this.listOfBooks);
                user.borrowBook(this.bookIndex + 1, borrowDate, returnDueDate);

                UserStorage.writeUserToTxt(userDataFile, listOfUsers);
                return;
            }
        }

        // If user not found, create a new user and add the borrowed book
        User newUser = new User(userName, listOfBooks);
        newUser.borrowBook(bookIndex + 1, borrowDate, returnDueDate);
        listOfUsers.add(newUser);
        UserStorage.writeUserToTxt(userDataFile, listOfUsers);
    }

    private Boolean isBookBorrowed(String userBorrowing, Book bookToBorrow) {
        for (int i = 0; i < listOfUsers.size(); i += 1) {
            User currentUser = listOfUsers.get(i);
            if (isBookBorrowedByCorrectUser(userBorrowing, bookToBorrow, currentUser)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isBookBorrowedByCorrectUser(String userBorrowing, Book bookToBorrow, User currentUser) {
        if (currentUser.getName().equals(userBorrowing)) {
            ArrayList<Book> currentUserBooks =  currentUser.getUserBooks();
            return isBookInUserBooks(bookToBorrow, currentUserBooks);
        }
        return false;
    }

    private static boolean isBookInUserBooks(Book bookToBorrow, ArrayList<Book> currentUserBooks) {
        for (int j = 0; j < currentUserBooks.size(); j += 1) {
            Book currentBook = currentUserBooks.get(j);
            if (currentBook.equals(bookToBorrow)) {
                return true;    // user has the book
            }
        }
        return false;
    }
}
