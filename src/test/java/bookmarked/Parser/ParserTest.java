package bookmarked.parser;

import bookmarked.Book;
import bookmarked.command.Command;
import bookmarked.exceptions.BookMarkedException;
import bookmarked.storage.BookStorage;
import bookmarked.storage.UserStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bookmarked.user.User;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
public class ParserTest {
    private static final String TEST_FILE_PATH = "./test.txt";
    private static final String TEST_USER_FILE_PATH = "./userTest.txt";
    private ArrayList<Book> listOfBooks;
    private String newItem;
    private File bookDataFile;
    private File userDataFile;
    private String[] splitItem;
    private Command userCommand;
    private ArrayList<User> listOfUsers;

    @BeforeEach
    public void init() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = BookStorage.createFile(TEST_FILE_PATH);
        userDataFile = UserStorage.createFile(TEST_USER_FILE_PATH);
        userCommand = null;
    }

    @Test
    public void parseCommand_invalidInput_bookMarkedException() {
        newItem = "";
        splitItem = newItem.split(" ");

        assertThrows(BookMarkedException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers, userDataFile);
        });

        newItem = "1";
        splitItem = newItem.split(" ");

        assertThrows(BookMarkedException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers, userDataFile);
        });

        newItem = "hello";
        splitItem = newItem.split(" ");
        assertThrows(BookMarkedException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers, userDataFile);
        });
    }

    @Test
    public void parseCommand_spaceInput_arrayIndexOutOfBoundsException() {
        newItem = "   ";
        splitItem = newItem.split(" ");

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            Parser.parseCommand(newItem, userCommand, listOfBooks, bookDataFile, splitItem, listOfUsers, userDataFile);
        });
    }


}
