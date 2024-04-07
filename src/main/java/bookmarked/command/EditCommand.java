package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.NoEditChangeException;
import bookmarked.exceptions.WrongInputFormatException;
import bookmarked.storage.BookStorage;
import bookmarked.ui.Ui;

import java.io.File;
import java.util.ArrayList;

/**
 * EditCommand class is to edit the details of the available book added in the library.
 */
public class EditCommand extends Command {
    private static final int BOOK_TO_EDIT_START_INDEX = 5;
    private static final int TITLE_START_INDEX = 7;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;
    private String userInput;
    private int bookNumberToEdit;
    private int numberOfEdits = 0;

    /**
     * Constructor for EditCommand, taking in user's input, array lists of books, and file for
     * book data storage.
     *
     * @param userInput user input to the terminal.
     * @param listOfBooks array list that stores all the books and its details in the library.
     * @param bookDataFile file for storing all the books and details.
     */
    public EditCommand(String userInput, ArrayList<Book> listOfBooks, File bookDataFile) {
        // Current book details
        this.listOfBooks = listOfBooks;
        this.bookDataFile = bookDataFile;
        this.userInput = userInput;
    }

    /**
     * Handles user's command to edit currently available book details in the library. If
     * description of what to edit is empty, book number to edit is not integer within the
     * range of the available books, or wrong input format, exceptions are handled.
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

            handleEditTitle(bookToEdit, bookNumberToEdit);

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
        }
    }

    public int getBookNumberToEdit(String bookToEditArgument) {
        return Integer.parseInt(bookToEditArgument);
    }

    public Book getBookToEdit(String bookToEditArgument, boolean isInputIndex) {
        Book bookToEdit = null;

        if (isInputIndex) {
            bookToEdit = listOfBooks.get(bookNumberToEdit - 1);
            return bookToEdit;
        }

        bookToEdit = getBookByTitleInput(bookToEditArgument, bookToEdit);
        return bookToEdit;
    }

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

    public void handleEditTitle(Book bookToEdit, int bookNumberToEdit)
            throws WrongInputFormatException, EmptyArgumentsException {
        String bookName;
        boolean isEditTitle = false;
        String[] splitInput = userInput.split(" ");
        isEditTitle = isEditTitle(splitInput, isEditTitle);

        if (isEditTitle) {
            int titleIndex = userInput.indexOf("/title");
            int nextSlash = userInput.indexOf("/", titleIndex + TITLE_START_INDEX);

            // if "/" is not found after the "/title"
            if (nextSlash == -1) {
                bookName = userInput.substring(titleIndex + TITLE_START_INDEX);
            } else {
                bookName = userInput.substring(titleIndex + TITLE_START_INDEX, nextSlash);
            }

            if (bookName.isBlank()) {
                throw new EmptyArgumentsException();
            }

            bookToEdit.setName(bookName);
            BookStorage.writeBookToTxt(bookDataFile, listOfBooks);
            Ui.printEditedBookConfirmation(bookNumberToEdit);
            numberOfEdits += 1;
        } else {
            throw new WrongInputFormatException();
        }
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
}
