package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.storage.BookStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {
    private static final String TEST_FILE_PATH = "./testBooks.txt";
    private String userInput;
    private String[] splitInput;
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;
    private DeleteCommand userCommand;

    @BeforeEach
    public void init() {
        listOfBooks = new ArrayList<>();
        bookDataFile = BookStorage.createFile(TEST_FILE_PATH);
        listOfBooks.add(new Book("book 1"));
        listOfBooks.add(new Book("book 2"));
        listOfBooks.add(new Book("book 3"));
    }

    @Test
    public void processDeleteCommand_noArgumentToDelete_emptyArgumentsException() {
        userInput = "delete";
        splitInput = userInput.split("delete");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);

        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void processDeleteCommand_spaceArgumentToDelete_emptyArgumentsException() {
        userInput = "delete          ";
        splitInput = userInput.split("delete");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });

        userInput = "delete ";
        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void processDeleteCommand_emptyList_emptyListException() {
        userInput = "delete 1";
        splitInput = userInput.split("delete");

        listOfBooks = new ArrayList<>();

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(EmptyListException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void handleCommand_validBookNumber_numberOfBookReduces() {
        userInput = "delete 1";
        splitInput = userInput.split("delete");

        int numberOfBooksBefore = listOfBooks.size();
        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        userCommand.handleCommand();
        int numberOfBooksAfter = listOfBooks.size();

        assertEquals(numberOfBooksBefore - 1, numberOfBooksAfter);
    }
}
