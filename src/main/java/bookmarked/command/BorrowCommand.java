package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.IndexOutOfListBounds;
import bookmarked.exceptions.InvalidStringException;
import bookmarked.exceptions.WrongInputFormatException;
import bookmarked.user.User;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;
import bookmarked.storage.UserStorage;

import bookmarked.arguments.InputValidity;
import bookmarked.arguments.SetBookIndexName;
import bookmarked.arguments.SetUserName;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;


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
    private static final String COMMAND_STRING = "borrow";
    private static final String ARGUMENT_STRING = " /by ";
    private String bookName;
    private String[] splitUser;
    private User currentUser;
    private int bookIndex = -1;
    private String userName;
    private String newItem;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;

    /**
     * Constructs a BorrowCommand with necessary book and user details.
     *
     * @param listOfBooks The list of books available for borrowing.
     * @param bookDataFile The data file where book data is stored.
     * @param listOfUsers The list of users in the library system.
     * @param newItem The new item to be added or command details.
     * @param userDataFile The data file where user data is stored.
     */
    public BorrowCommand(ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, String newItem, File userDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
    }

    /**
     * Handles the execution of the "borrow" command. It delegates to internal methods to set up arguments,
     * validate input, and process the borrowing of a book by a user.
     */
    @Override
    public void handleCommand() {
        try {
            setArguments();
        } catch (EmptyArgumentsException | InvalidStringException e) {
            Ui.printEmptyArgumentsMessage();
            return;
        } catch (BookNotFoundException e) {
            Ui.printBookNotFoundExceptionMessage();
            return;
        } catch (IndexOutOfListBounds e) {
            Ui.printOutOfBoundsMessage();
            return;
        }

        try {
            runBorrowCommand();
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        } catch (WrongInputFormatException e) {
            Ui.printWrongInputFormat();
        }
    }

    private static boolean isMoreThanOneBy(String[] splitParts) {
        return splitParts.length > 2;
    }

    /**
     * Validates the input provided for the borrow command and sets up the book and user arguments
     * for the borrowing process.
     *
     * @throws EmptyArgumentsException If the command arguments are empty.
     * @throws InvalidStringException If the book name string is invalid.
     * @throws BookNotFoundException If the specified book is not found in the list.
     * @throws IndexOutOfListBounds If the book index provided is out of bounds of the book list.
     */
    private void setArguments() throws EmptyArgumentsException, InvalidStringException,
            BookNotFoundException, IndexOutOfListBounds {
        try {
            inputValidity();
            setBookArguments();
            setUserArgument();
        } catch (InvalidStringException e) {
            throw new InvalidStringException();
        } catch (EmptyArgumentsException e) {
            throw new EmptyArgumentsException();
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        } catch (IndexOutOfListBounds e) {
            throw new IndexOutOfListBounds();

        }
    }


    /**
     * Processes the borrowing of a book by marking it as borrowed and setting the due date.
     * Informs the user if the book is already borrowed or if there are no available copies.
     *
     * @throws EmptyListException If the list of books is empty.
     * @throws WrongInputFormatException If the format of the input command is incorrect.
     */
    public void runBorrowCommand()
            throws EmptyListException, WrongInputFormatException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        Book bookToBorrow = this.listOfBooks.get(this.bookIndex);
        if (bookToBorrow.isAvailable() && !isBookBorrowed(this.userName, bookToBorrow)) {
            bookToBorrow.borrowBook(LocalDate.now(), DEFAULT_BORROW_PERIOD);
            updateListOfUsers(LocalDate.now());

            System.out.println("Borrowed " + this.bookName + " by " + userName + "!");
            System.out.println("Please return by " + bookToBorrow.getFormattedReturnDate() + ".");
        } else if (isBookBorrowed(userName, bookToBorrow)) {
            Ui.printUserAlreadyBorrowedBookMessage(userName);
        } else {
            Ui.printNoAvailableCopiesInInventoryMessage();
        }
    }

    /**
     * Updates the list of users with the current user's borrowing details, adding the newly borrowed
     * book and setting its due date.
     *
     * @param borrowDate The date on which the book is being borrowed.
     */
    private void updateListOfUsers(LocalDate borrowDate) {
        LocalDate returnDueDate = borrowDate.plus(DEFAULT_BORROW_PERIOD);
        this.currentUser.borrowBook(this.bookIndex, borrowDate, returnDueDate);
        UserStorage.writeUserToTxt(this.userDataFile, this.listOfUsers);
    }

    /**
     * Validates the format and contents of the command input to ensure it adheres to the expected
     * structure and contains all necessary information.
     *
     * @throws InvalidStringException If the input string is invalid.
     * @throws EmptyArgumentsException If the input string is empty.
     */
    public void inputValidity() throws InvalidStringException, EmptyArgumentsException {
        try {
            InputValidity inputValidity = new InputValidity(COMMAND_STRING, this.newItem, ARGUMENT_STRING);
            inputValidity.checkInputValidity();
            this.splitUser = inputValidity.getSplitArgument();
        } catch (InvalidStringException e) {
            throw new InvalidStringException();
        } catch (EmptyArgumentsException e) {
            throw new EmptyArgumentsException();
        }
    }

    /**
     * Sets the book arguments for the borrow command. Determines which book the user wants to borrow
     * based on either an index or a book title.
     *
     * @throws BookNotFoundException If the specified book is not found in the list.
     * @throws IndexOutOfListBounds If the book index provided is out of bounds of the book list.
     */
    public void setBookArguments() throws BookNotFoundException, IndexOutOfListBounds {
        try {
            SetBookIndexName setBookIndexName = new SetBookIndexName(splitUser[0].trim(), listOfBooks);
            setBookIndexName.setArguments();
            this.bookIndex = setBookIndexName.getBookIndex();
            this.bookName = setBookIndexName.getBookName();
        } catch (BookNotFoundException e) {
            throw new BookNotFoundException();
        } catch (IndexOutOfListBounds e) {
            throw new IndexOutOfListBounds();
        }
    }


    public void setUserArgument() {
        SetUserName setUserName = new SetUserName(this.splitUser[1].trim(), listOfUsers);
        this.currentUser = setUserName.checkBorrowUserNameValidity(listOfBooks, userDataFile);
        this.userName = this.currentUser.getName();
    }

    /**
     * Checks if the specified book is already borrowed by the given user.
     *
     * @param userBorrowing The name of the user borrowing the book.
     * @param bookToBorrow The book to check if borrowed.
     * @return True if the book is already borrowed by the user; false otherwise.
     */
    private Boolean isBookBorrowed(String userBorrowing, Book bookToBorrow) {
        for (int i = 0; i < listOfUsers.size(); i += 1) {
            if (isBookBorrowedByCorrectUser(userBorrowing, bookToBorrow, this.currentUser)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the specified book is borrowed by the correct user.
     *
     * @param userBorrowing The name of the user to check against.
     * @param bookToBorrow The book to be checked.
     * @param currentUser The current user attempting to borrow the book.
     * @return True if the book is borrowed by the user; false otherwise.
     */
    private static boolean isBookBorrowedByCorrectUser(String userBorrowing, Book bookToBorrow, User currentUser) {
        if (currentUser.getName().equals(userBorrowing)) {
            ArrayList<Book> currentUserBooks = currentUser.getUserBooks();
            return isBookInUserBooks(bookToBorrow, currentUserBooks);
        }
        return false;
    }

    /**
     * Checks if the specified book is in the list of books borrowed by a user.
     *
     * @param bookToBorrow The book to be checked.
     * @param currentUserBooks The list of books borrowed by the user.
     * @return True if the book is in the user's list of borrowed books; false otherwise.
     */
    private static boolean isBookInUserBooks(Book bookToBorrow, ArrayList<Book> currentUserBooks) {
        for (Book currentBook : currentUserBooks) {
            if (currentBook.equals(bookToBorrow)) {
                return true;    // user has the book
            }
        }
        return false;
    }

}
