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

import java.io.File;
import java.util.ArrayList;

/**
 * Handles the extension of the borrowing period for books in a library. This command
 * allows users to request additional time for books they have currently borrowed.
 */
public class ExtendCommand extends Command {
    private String bookName = null;
    private String newItem;
    private String[] splitUser;
    private User currentUser;
    private int bookIndex = -1; // Index starting from 0
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;


    /**
     * Constructs an ExtendCommand with the specified user input, book and user lists, and data storage files.
     *
     * @param newItem      The user input containing the command details.
     * @param listOfBooks  The list of books from which a book will be extended.
     * @param bookDataFile The file where book data is stored.
     * @param listOfUsers  The list of users who may have borrowed books.
     * @param userDataFile The file where user data is stored.
     */
    public ExtendCommand(String newItem, ArrayList<Book> listOfBooks, File bookDataFile,
                         ArrayList<User> listOfUsers, File userDataFile) {
        this.newItem = newItem;
        this.listOfBooks = listOfBooks;
        this.listOfUsers = listOfUsers;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
        this.bookName = null;
    }


    /**
     * Processes the extend command by validating user input and extending the due date of a borrowed book.
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
            runExtendCommand();
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
        } catch (EmptyListException e) {
            Ui.printEmptyListMessage();
        }
    }


    /**
     * Runs the extension logic by checking if the book is currently borrowed by the user
     * and then extending the due date. Notifies the user of the outcome.
     *
     * @throws EmptyListException If no books are available in the library to extend.
     */
    private void runExtendCommand() throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        Book extendingBook = this.listOfBooks.get(this.bookIndex);
        if (hasUserBorrowedBook()) {
            extendingBook.extendDueDate();
            extendBookInUserDate();
            UserStorage.writeUserToTxt(userDataFile, listOfUsers);
            Ui.printExtensionSuccessMessage(this.bookName);
        } else {
            Ui.printBookNotBorrowedByUserMessage(this.currentUser.getName());
        }
    }

    private void extendBookInUserDate() {
        for (int i = 0; i < listOfUsers.size(); i += 1) {
            User currentUserInList = listOfUsers.get(i);
            if (currentUserInList.equals(this.currentUser)) {
                currentUserInList.extendDueDate(this.bookIndex);
            }
        }
    }

    private void updateBookIndex(ArrayList<Book> listOfBooks) throws BookNotFoundException {
        if (!doesBookExists()) {
            throw new BookNotFoundException();
        }

        for (int i = 0; i < listOfBooks.size(); i += 1) {
            String currentBookName = listOfBooks.get(i).getName();
            if (currentBookName.equals(bookName)) {
                this.bookIndex = i;
            }
        }
    }

    /**
     * Processes arguments from the input string and sets internal variables for the book
     * index and user. Throws appropriate exceptions if inputs are invalid or not found.
     *
     * @throws InvalidStringException    If the command format is incorrect.
     * @throws EmptyArgumentsException   If critical arguments are missing.
     * @throws UserNotFoundException     If the user specified cannot be found.
     * @throws BookNotFoundException     If the book specified cannot be found.
     * @throws IndexOutOfListBounds      If the specified index is out of the list bounds.
     */
    public void setArguments() throws InvalidStringException, UserNotFoundException,
            BookNotFoundException, EmptyArgumentsException, IndexOutOfListBounds {

        String[] splitParts = this.newItem.split("extend");
        if (splitParts.length < 1) {
            throw new InvalidStringException();
        }

        this.splitUser = splitParts[1].split(" /by ");

        if (this.splitUser.length < 2 || this.splitUser[1].isBlank()) {
            throw new InvalidStringException();
        }

        if (this.splitUser[0].isBlank()) {
            throw new EmptyArgumentsException();
        }

        boolean isInputIndex;
        try {
            this.bookIndex = checkBookIndexValidity();
            this.bookName = listOfBooks.get(this.bookIndex).getName();
            isInputIndex = true;
        } catch (NumberFormatException e) {
            this.bookName = (splitUser[0].trim());
            isInputIndex = false;
        } catch (IndexOutOfListBounds e) {
            throw new IndexOutOfListBounds();
        }

        if (!isInputIndex) {
            try {
                updateBookIndex(listOfBooks);
            } catch (BookNotFoundException e) {
                throw new BookNotFoundException();
            }
        }

        try {
            this.currentUser = checkUserNameValidity();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
    }

    /**
     * Checks if the specified book index is valid within the current list of books.
     *
     * @return The validated book index adjusted for zero-based indexing.
     * @throws IndexOutOfListBounds If the index is outside the allowable range.
     */
    public int checkBookIndexValidity() throws IndexOutOfListBounds {
        int bookIndex = Integer.parseInt(splitUser[0].trim());
        if (bookIndex < 0 || bookIndex > this.listOfBooks.size()) {
            throw new IndexOutOfListBounds();
        }
        return bookIndex - 1;
    }

    /**
     * Confirms if the specified user name matches an existing user in the system.
     *
     * @return The User object if found.
     * @throws UserNotFoundException If the user cannot be found.
     */
    public User checkUserNameValidity() throws UserNotFoundException {
        String userString = this.splitUser[1].trim();
        for (User user : listOfUsers) {
            if (user.getName().matches(userString)) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }

    /**
     * Determines if the book in question exists in the list of books.
     *
     * @return true if the book exists, otherwise false.
     */
    public boolean doesBookExists() {
        for (Book book : listOfBooks) {
            if (book.getName().matches(bookName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current user has indeed borrowed the specified book.
     *
     * @return true if the book is currently borrowed by the user, otherwise false.
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

