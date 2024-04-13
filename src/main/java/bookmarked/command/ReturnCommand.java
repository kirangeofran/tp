package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;

import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.InvalidStringException;
import bookmarked.exceptions.UserNotFoundException;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.IndexOutOfListBounds;

import bookmarked.storage.BookStorage;
import bookmarked.storage.UserStorage;
import bookmarked.ui.Ui;

import bookmarked.arguments.InputValidity;
import bookmarked.arguments.SetBookIndexName;
import bookmarked.arguments.SetUserName;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

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
    private final String COMMAND_STRING = "return";
    private final String ARGUMENT_STRING = " /by ";
    private String bookName;
    private String newItem;
    private String[] splitUser;
    private User currentUser;
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
     * @param newItem      The user input
     *                     and any subsequent elements are the remaining parts of the book name if applicable.
     * @param listOfBooks  The list of books from which a book will be returned.
     * @param bookDataFile The data file where books are stored.
     */
    public ReturnCommand(String newItem, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, File userDataFile) {
        assert newItem != null : "commandParts should not be null";

        this.newItem = newItem;
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
        try {
            setArguments();
        } catch (InvalidStringException | EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
            return;
        } catch (UserNotFoundException e) {
            Ui.printNotExistingUserMessage();
            return;
        } catch (BookNotFoundException e) {
            Ui.printBookNotFoundExceptionMessage();
            return;
        } catch (IndexOutOfListBounds e) {
            Ui.printOutOfBoundsMessage();
            return;
        }

        try {
            runReturnCommand();
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }


    public void setArguments() throws InvalidStringException, UserNotFoundException,
            BookNotFoundException, EmptyArgumentsException, IndexOutOfListBounds {

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
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }


    /**
     * Marks the books found in the list as returned. This method handles the case where multiple
     * copies of a book exist by marking all matched copies as returned. It also updates the status
     * of the book in the system and notifies the user of the action taken. If no books match
     * the provided identifier (name or index), it informs the user that the book was not found.
     *
     * @throws EmptyListException If the overall list of books is empty, indicating a system error.
     */
    public void runReturnCommand() throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        Book returningBook = this.listOfBooks.get(this.bookIndex);
        if (hasUserBorrowedBook()) {
            returningBook.setReturned();
            this.currentUser.unborrowBook(this.bookIndex);
            removeCurrentUserIfNoBookBorrowed();
            UserStorage.writeUserToTxt(userDataFile, listOfUsers);
            System.out.println(returningBook.getReturnDate());
            System.out.println(Book.isOverdue(returningBook.getReturnDate()));
            if (Book.isOverdue(returningBook.getReturnDate())) {
                Ui.bookIsOverdue();
            }
            System.out.println("Returned " + returningBook.getName() + "!");
        } else {
            Ui.printBookNotBorrowedByUserMessage(this.currentUser.getName());
        }
    }

    public void removeCurrentUserIfNoBookBorrowed() {
        Iterator<User> iterator = listOfUsers.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.equals(this.currentUser) && user.getUserBooks().isEmpty()) {
                iterator.remove();
            }
        }
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


    public void setUserArgument() throws UserNotFoundException {
        try {
            SetUserName setUserName = new SetUserName(this.splitUser[1].trim(), listOfUsers);
            this.currentUser = setUserName.checkUserNameValidity();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }


    public boolean hasUserBorrowedBook() {
        for (Book book : this.currentUser.getUserBooks()) {
            if (book.getName().matches(this.bookName)) {
                return true;
            }
        }
        return false;
    }

}
