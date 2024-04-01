package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.storage.BookStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EditCommandTest {
    private static final String TEST_FILE_PATH = "./test.txt";
    private Book bookToEdit;
    private int bookNumberToEdit;
    private String userInput;
    private EditCommand userCommand;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;

    @BeforeEach
    public void init() {
        listOfBooks = new ArrayList<>();
        bookDataFile = BookStorage.createFile(TEST_FILE_PATH);
        listOfBooks.add(new Book("book 1"));
        listOfBooks.add(new Book("book 2"));
        listOfBooks.add(new Book("book 3"));
        bookNumberToEdit = 1;
    }

    @Test
    public void handleEditTitle_spaceBookName_emptyArgumentException() {
        userInput = "edit 1 /title ";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);
        bookToEdit = listOfBooks.get(bookNumberToEdit - 1);

        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.handleEditTitle(bookToEdit, bookNumberToEdit);
        });

        userInput = "edit 1 /title             ";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.handleEditTitle(bookToEdit, bookNumberToEdit);
        });
    }

    @Test
    public void handleEditTitle_emptyBookName_stringIndexOutOfBoundsException() {
        userInput = "edit 1 /title";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);
        bookToEdit = listOfBooks.get(bookNumberToEdit - 1);

        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            userCommand.handleEditTitle(bookToEdit, bookNumberToEdit);
        });
    }

    @Test
    public void handleCommand_nonIntegerBookNumber_NumberFormatExceptionCaught() {
        userInput = "edit x /title book";
        Command userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Please enter a book number in integer format";
            assertEquals(e.getMessage(), errorMessage);
        }

        userInput = "edit #*? /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Please enter a book number in integer format";
            assertEquals(e.getMessage(), errorMessage);
        }

        userInput = "edit 1.5 /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Please enter a book number in integer format";
            assertEquals(e.getMessage(), errorMessage);
        }

        userInput = "edit   /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Please enter a book number in integer format";
            assertEquals(e.getMessage(), errorMessage);
        }
    }

    @Test
    public void handleCommand_invalidIntegerBookNumber_IndexOutOfBoundsExceptionCaught() {
        userInput = "edit 10 /title book";
        Command userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Sorry,, no book number of: " + bookNumberToEdit;
            assertEquals(e.getMessage(), errorMessage);
        }

        userInput = "edit 0 /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Sorry, no book number of: " + bookNumberToEdit;
            assertEquals(e.getMessage(), errorMessage);
        }

        userInput = "edit -5 /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile);

        try {
            userCommand.handleCommand();
        } catch (NumberFormatException e) {
            String errorMessage = "Sorry, no book number of: " + bookNumberToEdit;
            assertEquals(e.getMessage(), errorMessage);
        }
    }
}
