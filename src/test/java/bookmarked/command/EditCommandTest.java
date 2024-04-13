package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
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
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private String bookToEditArgument;

    @BeforeEach
    public void init() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = BookStorage.createFile(TEST_FILE_PATH);
        listOfBooks.add(new Book("book 1"));
        listOfBooks.add(new Book("book 2"));
        listOfBooks.add(new Book("book 3"));
        bookNumberToEdit = 1;
    }

    @Test
    public void handleEditTitle_spaceBookName_emptyArgumentException() {
        userInput = "edit 1 /title ";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);
        bookToEdit = listOfBooks.get(bookNumberToEdit - 1);

        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.handleEditTitle(bookToEdit, bookNumberToEdit);
        });

        userInput = "edit 1 /title             ";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);
        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.handleEditTitle(bookToEdit, bookNumberToEdit);
        });
    }

    @Test
    public void handleEditTitle_emptyBookName_stringIndexOutOfBoundsException() {
        userInput = "edit 1 /title";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);
        bookToEdit = listOfBooks.get(bookNumberToEdit - 1);

        assertThrows(StringIndexOutOfBoundsException.class, () -> {
            userCommand.handleEditTitle(bookToEdit, bookNumberToEdit);
        });
    }

    @Test
    public void getBookNumberToEdit_nonIntegerBookNumber_numberFormatExceptionCaught() {
        userInput = "edit x /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);

        assertThrows(NumberFormatException.class, () -> {
            userCommand.getBookNumberToEdit("x");
        });

        userInput = "edit #*? /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);

        assertThrows(NumberFormatException.class, () -> {
            userCommand.getBookNumberToEdit("#*?");
        });

        userInput = "edit 1.5 /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);

        assertThrows(NumberFormatException.class, () -> {
            userCommand.getBookNumberToEdit("1.5");
        });

        userInput = "edit   /title book";
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);

        assertThrows(NumberFormatException.class, () -> {
            userCommand.getBookNumberToEdit("  ");
        });
    }

    @Test
    public void getBookToEdit_invalidIntegerBookNumber_indexOutOfBoundsExceptionCaught() {
        userInput = "edit 10 /title book";
        bookNumberToEdit = 10;
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);
        boolean isInputIndex = true;

        bookToEditArgument = "10";
        assertThrows(IndexOutOfBoundsException.class, () -> {
            userCommand.getBookToEdit(bookToEditArgument, isInputIndex);
        });

        userInput = "edit 0 /title book";
        bookNumberToEdit = 0;
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);

        bookToEditArgument = "0";
        assertThrows(IndexOutOfBoundsException.class, () -> {
            userCommand.getBookToEdit(bookToEditArgument, isInputIndex);
        });

        userInput = "edit -5 /title book";
        bookNumberToEdit = -5;
        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);

        bookToEditArgument = "-5";
        assertThrows(IndexOutOfBoundsException.class, () -> {
            userCommand.getBookToEdit(bookToEditArgument, isInputIndex);
        });
    }

    @Test
    public void handleCommand_validInputArgument_editCorrectly() {
        bookNumberToEdit = 1;
        userInput = "edit 1 /title book";
        Book book2 = listOfBooks.get(1);
        Book book3 = listOfBooks.get(2);

        userCommand = new EditCommand(userInput, listOfBooks, bookDataFile, listOfUsers);
        userCommand.handleCommand();
        Book bookAfterEdit = listOfBooks.get(bookNumberToEdit - 1);
        Book book2AfterEdit = listOfBooks.get(1);
        Book book3AfterEdit = listOfBooks.get(2);

        assertEquals("book", bookAfterEdit.getName());

        // Test the rest are unchanged
        assertEquals(book2.getName(), book2AfterEdit.getName());
        assertEquals(book3.getName(), book3AfterEdit.getName());
    }
}
