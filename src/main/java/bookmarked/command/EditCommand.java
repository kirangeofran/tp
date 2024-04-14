package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.ExistedBookNameException;
import bookmarked.exceptions.NoEditChangeException;
import bookmarked.exceptions.SameBookNameException;
import bookmarked.exceptions.WrongInputFormatException;
import bookmarked.storage.BookStorage;
import bookmarked.storage.UserStorage;
import bookmarked.ui.Ui;
import bookmarked.user.User;
import bookmarked.userBook.UserBook;

import java.io.File;
import java.util.ArrayList;

/**
 * Handles the edit command to modify details of books available in the library.
 * This command allows for updating the title or other attributes of a book based on user input.
 */
public class EditCommand extends Command {
    private static final int BOOK_TO_EDIT_START_INDEX = 5;
    private static final int TITLE_START_INDEX = 7;
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;
    private String userInput;
    private int bookNumberToEdit;
    private int numberOfEdits = 0;

    /**
     * Constructs an EditCommand with specified user input and context for book editing.
     *
     * @param userInput    The raw user input containing details for the edit operation.
     * @param listOfBooks  A list containing all the books available in the library.
     * @param bookDataFile A file used to store book data persistently.
     * @param listOfUsers  A list of users who may have interactions with books.
     */
    public EditCommand(String userInput, ArrayList<Book> listOfBooks, File bookDataFile,
                       File userDataFile, ArrayList<User> listOfUsers) {
        // Current book details
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.userDataFile = userDataFile;
        this.userInput = userInput;
        this.listOfUsers = listOfUsers;
    }

    /**
     * Processes the command to edit book details. It checks for command format and handles various
     * editing scenarios based on the input provided.
     */
    @Override
    public void handleCommand() {
        boolean isInputIndex = true;
        String bookToEditArgument = null;

        try {
            if (!userInput.contains("/")) {
                throw new NoEditChangeException();
            }

            int firstSlashIndex = userInput.indexOf(" /");
            bookToEditArgument = userInput.substring(BOOK_TO_EDIT_START_INDEX, firstSlashIndex).strip();
            bookNumberToEdit = getBookNumberToEdit(bookToEditArgument);
        } catch (NumberFormatException e) {
            isInputIndex = false;
        } catch (NoEditChangeException e) {
            Ui.printNoEditChangeException();
            return;
        } catch (IndexOutOfBoundsException e) { // wrong format input (e.g. edit 1/title book)
            Ui.printWrongInputFormat();
            return;
        }

        handleBookEdit(bookToEditArgument, isInputIndex);
    }

    private void handleBookEdit(String bookToEditArgument, boolean isInputIndex) {
        Book bookToEdit;
        try {
            bookToEdit = getBookToEdit(bookToEditArgument, isInputIndex);
            if (bookToEdit == null) {
                throw new BookNotFoundException();
            }

            handleEditTitle(bookToEdit);

            if (numberOfEdits == 0) {
                Ui.printEmptyArgumentsMessage();
            }

        } catch (StringIndexOutOfBoundsException | EmptyArgumentsException e) {
            Ui.printEmptyArgumentsMessage();
        } catch (IndexOutOfBoundsException e) {
            Ui.printOutOfBoundsMessage();
        } catch (NullPointerException e) {
            Ui.printIncorrectInputFormat();
        } catch (BookNotFoundException e) {
            Ui.printBookNotFoundExceptionMessage();
        } catch (WrongInputFormatException e) {
            Ui.printWrongInputFormat();
        } catch (ExistedBookNameException e) {
            Ui.printDuplicateTitleMessage();
        } catch (SameBookNameException e) {
            Ui.printEditSameBookExceptionMessage();
        }
    }

    /**
     * Retrieves the index number of the book to edit based on user input.
     *
     * @param bookToEditArgument The part of the user input specifying the book index or title.
     * @return The index of the book to edit.
     */
    public int getBookNumberToEdit(String bookToEditArgument) {
        return Integer.parseInt(bookToEditArgument);
    }

/**
 * Retrieves the Book object to be edited either by index or by title.
 *
 * @param bookToEditArgument The input string indicating the book identifier.
 * @param isInputIndex       A boolean indicating if the identifier is an index.
 * @return The Book object to be edited.
 */
    public Book getBookToEdit(String bookToEditArgument, boolean isInputIndex) {
        Book bookToEdit = null;

        if (isInputIndex) {
            bookToEdit = listOfBooks.get(bookNumberToEdit - 1);
            return bookToEdit;
        }

        bookToEdit = getBookByTitleInput(bookToEditArgument, bookToEdit);
        return bookToEdit;
    }

/**
 * Finds a book in the list by its title.
 *
 * @param bookToEditArgument The title of the book as provided by the user.
 * @return The found Book object, or null if no book matches the title.
 */
    private Book getBookByTitleInput(String bookToEditArgument, Book bookToEdit) {
        Book inputBook = new Book(bookToEditArgument);
        for (int i = 0; i < listOfBooks.size(); i += 1) {
            if (listOfBooks.get(i).equals(inputBook)) {
                bookToEdit = listOfBooks.get(i);
                bookNumberToEdit = i + 1;
            }
        }
        return bookToEdit;
    }

    /**
     * Handles the editing of a book's title based on user input.
     *
     * @param bookToEdit        The book whose title is to be edited.
     * @throws WrongInputFormatException If the format of the edit command is incorrect.
     * @throws EmptyArgumentsException   If the new title is empty or missing.
     */
    public void handleEditTitle(Book bookToEdit)
            throws WrongInputFormatException, EmptyArgumentsException,
            SameBookNameException, ExistedBookNameException {
        String oldName = bookToEdit.getName();
        String newBookName;
        boolean isEditTitle = false;
        String[] splitInput = userInput.split(" ");
        isEditTitle = isEditTitle(splitInput, isEditTitle);

        if (isEditTitle) {
            int titleIndex = userInput.indexOf("/title");
            int nextSlash = userInput.indexOf("/", titleIndex + TITLE_START_INDEX);

            // if "/" is not found after the "/title"
            if (nextSlash == -1) {
                newBookName = userInput.substring(titleIndex + TITLE_START_INDEX).strip();
            } else {
                newBookName = userInput.substring(titleIndex + TITLE_START_INDEX, nextSlash).strip();
            }

            isValidBookName(bookToEdit, newBookName);

            updateUserBooks(bookToEdit.getName(), newBookName);
            bookToEdit.setName(newBookName);
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
            UserStorage.writeUserToTxt(userDataFile, listOfUsers);
            Ui.printEditedBookConfirmation(newBookName, oldName);
            numberOfEdits += 1;
        } else {
            throw new WrongInputFormatException();
        }
    }

    private void isValidBookName(Book bookToEdit, String newBookName) throws EmptyArgumentsException, SameBookNameException, ExistedBookNameException {
        if (newBookName.isBlank()) {
            throw new EmptyArgumentsException();
        }

        if (newBookName.equals(bookToEdit.getName())) {
            throw new SameBookNameException();
        }

        if (isExistedBookName(newBookName)) {
            throw new ExistedBookNameException();
        }
    }

    private boolean isExistedBookName(String bookName) {
        for (Book currentBook : this.listOfBooks) {
            if (bookName.equals(currentBook.getName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEditTitle(String[] splitInput, boolean isEditTitle) {
        for (String word : splitInput) {
            if (word.equals("/title")) {
                isEditTitle = true;
                break;
            }
        }
        return isEditTitle;
    }

    /**
     * Updates the records of all users who may have interactions with the edited book.
     *
     * @param oldTitle The old title of the book.
     * @param newTitle The new title of the book.
     */
    private void updateUserBooks(String oldTitle, String newTitle) {
        for (User currentUser : this.listOfUsers) {
            ArrayList<UserBook> currentUserBooksList = currentUser.getListOfUserBooks();
            currentUser.editBook(oldTitle, newTitle, currentUserBooksList);
        }
    }
}
