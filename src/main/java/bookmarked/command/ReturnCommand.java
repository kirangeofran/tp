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
import bookmarked.userbook.UserBook;

import java.io.File;
import java.time.LocalDate;
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
    private static final String COMMAND_STRING = "return";
    private static final String ARGUMENT_STRING = " /by ";
    private final String newItem;
    private String bookName;
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

    /**
     * Sets the details of Book and User class based on the arguments
     *
     * @throws InvalidStringException If the user input is not in the correct argument format
     * @throws UserNotFoundException If user's name input does not exist in the system
     * @throws BookNotFoundException If book input does not exist in the system
     * @throws EmptyArgumentsException If no input is provided
     * @throws IndexOutOfListBounds If input index is not within the range of the number of books in the library
     */
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
        LocalDate returnDate = findReturnDate(returningBook);
        if (hasUserBorrowedBook()) {
            if (Book.isOverdue(returnDate)) {
                Ui.bookIsOverdue();
            }
            returningBook.setReturned();
            this.currentUser.unborrowBook(this.bookIndex);
            removeCurrentUserIfNoBookBorrowed();
            UserStorage.writeUserToTxt(userDataFile, listOfUsers);
            System.out.println("Returned " + returningBook.getName() + "!");
        } else {
            Ui.printBookNotBorrowedReturnMessage(this.currentUser.getName());
        }
    }

    /**
     * finds the return date of the intended book
     * iterates through the user book list to find book matching the returning book
     * @param returningBook the book being returned
     * @return returnDate the return date of the book
     */
    public LocalDate findReturnDate(Book returningBook) {
        ArrayList<UserBook> listOfUserBooks = this.currentUser.getListOfUserBooks();
        for (UserBook books : listOfUserBooks) {
            if (books.getUserBookTitle().equalsIgnoreCase(returningBook.getName())) {
                return books.getReturnDueDate();
            }
        }
        return null;
    }

    /**
     * removes a user from the user list if they have no more books borrowed. Checks
     * after returning book, if there are no borrowed books in their userbook list
     */
    public void removeCurrentUserIfNoBookBorrowed() {
        Iterator<User> iterator = listOfUsers.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.equals(this.currentUser) && user.getUserBooks().isEmpty()) {
                iterator.remove();
            }
        }
    }

    /**
     * inputValidity checks whether input is valid and set the splitUser into the correct split Argument
     * based on the input
     * @throws InvalidStringException If the user input is not in the correct argument format
     * @throws EmptyArgumentsException If no input is provided
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
     * Set the details in the Book class based on the arguments
     * @throws BookNotFoundException If book input does not exist in the system
     * @throws IndexOutOfListBounds If input index is not within the range of the number of books in the library
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

    /**
     * Set the details in the User class based on the arguments
     * @throws UserNotFoundException If user's name input does not exist in the system
     */
    public void setUserArgument() throws UserNotFoundException {
        try {
            SetUserName setUserName = new SetUserName(this.splitUser[1].trim(), listOfUsers);
            this.currentUser = setUserName.checkUserNameValidity();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    /**
     * Find out whether a user borrowed a given book
     * @return boolean true if user borrowed the given book, false otherwise
     */
    public boolean hasUserBorrowedBook() {
        for (Book book : this.currentUser.getUserBooks()) {
            if (book.getName().matches(this.bookName)) {
                return true;
            }
        }
        return false;
    }

}
