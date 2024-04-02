package bookmarked.Parser;

import bookmarked.Book;
import bookmarked.command.Command;
import bookmarked.exceptions.BookMarkedException;
import bookmarked.parser.Parser;
import bookmarked.storage.BookStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bookmarked.User;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
public class ParserTest {
    private static final String TEST_FILE_PATH = "./test.txt";
    private ArrayList<Book> listOfBooks;
    private String newItem;
    private File bookDataFile;
    private String[] splitItem;
    private Command userCommand;
    private ArrayList<User> listOfUsers;

    @BeforeEach
    public void init() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = BookStorage.createFile(TEST_FILE_PATH);
        userCommand = null;
    }

    @Test
    public void parseCommand_invalidInput_bookMarkedException() {
        newItem = "";
        splitItem = newItem.split(" ");

        assertThrows(BookMarkedException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers);
        });

        newItem = "1";
        splitItem = newItem.split(" ");

        assertThrows(BookMarkedException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers);
        });

        newItem = "hello";
        splitItem = newItem.split(" ");
        assertThrows(BookMarkedException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers);
        });
    }

    @Test
    public void parseCommand_spaceInput_arrayIndexOutOfBoundsException() {
        newItem = "   ";
        splitItem = newItem.split(" ");

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers);
        });
    }
}
