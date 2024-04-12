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
 * The {@code ExtendCommand} class handles the extension of the borrowing period for a book.
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
     * Constructs an {ExtendCommand} with the specified command parts, list of books, and file.
     *
     * @param listOfBooks  the current list of books
     * @param bookDataFile the file where the list of books is stored
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
     * Executes the extend command by trying to extend the due date of a borrowed book.
     * It catches specific exceptions and delegates error messages to the {@code Ui} class.
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
     * Runs the logic to extend the due date of a borrowed book. If the book is not found
     * or is not currently borrowed, it throws corresponding exceptions.
     */
    private void runExtendCommand() throws EmptyListException {
        if (this.listOfBooks.isEmpty()) {
            throw new EmptyListException();
        }

        Book extendingBook = this.listOfBooks.get(this.bookIndex);
        if (hasUserBorrowedBook()) {
            extendingBook.extendDueDate();
            UserStorage.writeUserToTxt(userDataFile, listOfUsers);
            System.out.println("Due date for '" + this.bookName + "' has been extended by a week.");
        } else {
            Ui.printBookNotBorrowedByUserMessage(this.currentUser.getName());
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


    public int checkBookIndexValidity() throws IndexOutOfListBounds {
        int bookIndex = Integer.parseInt(splitUser[0].trim());
        if (bookIndex < 0 || bookIndex > this.listOfBooks.size()) {
            throw new IndexOutOfListBounds();
        }
        return bookIndex - 1;
    }


    public User checkUserNameValidity() throws UserNotFoundException {
        String userString = this.splitUser[1].trim();
        for (User user : listOfUsers) {
            if (user.getName().matches(userString)) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }


    public boolean doesBookExists() {
        for (Book book : listOfBooks) {
            if (book.getName().matches(bookName)) {
                return true;
            }
        }
        return false;
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

