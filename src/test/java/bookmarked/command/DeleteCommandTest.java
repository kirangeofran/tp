package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.IndexOutOfListBounds;
import bookmarked.storage.BookStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteCommandTest {
    private static final String TEST_FILE_PATH = "./test.txt";
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
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);

        assertThrows(EmptyArgumentsException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void processDeleteCommand_spaceArgumentToDelete_emptyArgumentsException() {
        userInput = "delete          ";
        splitInput = userInput.split(" ");

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
    public void processDeleteCommand_nonIntegerArgument_numberFormatException() {
        userInput = "delete a";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(NumberFormatException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });

        userInput = "delete 1.5";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(NumberFormatException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });

        userInput = "delete book 1";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(NumberFormatException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });

        userInput = "delete #*!";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(NumberFormatException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void processDeleteCommand_unavailableBookNumber_indexOutOfListBounds() {
        userInput = "delete 100";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(IndexOutOfListBounds.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });

        userInput = "delete 0";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(IndexOutOfListBounds.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });

        userInput = "delete -1";
        splitInput = userInput.split(" ");

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(IndexOutOfListBounds.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void processDeleteCommand_emptyList_emptyListException() {
        userInput = "delete 1";
        splitInput = userInput.split(" ");

        listOfBooks = new ArrayList<>();

        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        assertThrows(EmptyListException.class, () -> {
            userCommand.processDeleteCommand(splitInput);
        });
    }

    @Test
    public void handleCommand_validBookNumber_numberOfBookReduces() {
        userInput = "delete 1";
        splitInput = userInput.split(" ");

        int numberOfBooksBefore = listOfBooks.size();
        userCommand = new DeleteCommand(userInput, listOfBooks, bookDataFile);
        userCommand.handleCommand();
        int numberOfBooksAfter = listOfBooks.size();

        assertEquals(numberOfBooksBefore - 1, numberOfBooksAfter);
    }
}
