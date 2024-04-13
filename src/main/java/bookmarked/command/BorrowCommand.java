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
    private final String COMMAND_STRING = "borrow";
    private final String ARGUMENT_STRING = " /by ";
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

    public BorrowCommand(ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, String newItem, File userDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
    }


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
     * Attempts to mark a book as borrowed based on its availability. If the book is available,
     * it is marked as borrowed, and a due date is set. If the book is currently borrowed, the user is
     * informed of the expected return date. This method updates the user's list of borrowed books accordingly.
     *
     * @throws EmptyListException        If the overall list of books is empty.
     * @throws WrongInputFormatException If the input format is incorrect.
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
     * updates the list of users by adding the new user and its borrowed books to the user's list
     * Checks whether the user has already borrowed books and hence is already in the user list
     *
     */
    private void updateListOfUsers(LocalDate borrowDate) {
        LocalDate returnDueDate = borrowDate.plus(DEFAULT_BORROW_PERIOD);
        this.currentUser.borrowBook(this.bookIndex, borrowDate, returnDueDate);
        UserStorage.writeUserToTxt(this.userDataFile, this.listOfUsers);
    }


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


    private Boolean isBookBorrowed(String userBorrowing, Book bookToBorrow) {
        for (int i = 0; i < listOfUsers.size(); i += 1) {
            if (isBookBorrowedByCorrectUser(userBorrowing, bookToBorrow, this.currentUser)) {
                return true;
            }
        }

        return false;
    }


    private static boolean isBookBorrowedByCorrectUser(String userBorrowing, Book bookToBorrow, User currentUser) {
        if (currentUser.getName().equals(userBorrowing)) {
            ArrayList<Book> currentUserBooks = currentUser.getUserBooks();
            return isBookInUserBooks(bookToBorrow, currentUserBooks);
        }
        return false;
    }


    private static boolean isBookInUserBooks(Book bookToBorrow, ArrayList<Book> currentUserBooks) {
        for (Book currentBook : currentUserBooks) {
            if (currentBook.equals(bookToBorrow)) {
                return true;    // user has the book
            }
        }
        return false;
    }

}
