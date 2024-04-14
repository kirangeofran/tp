package bookmarked.command;

import bookmarked.Book;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddCommandTest {

    @Test
    public void addCommand_emptyArguments_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        File bookDataFile = null;
        AddCommand addCommand = new AddCommand("", listOfBooks, bookDataFile);
        assertThrows(IndexOutOfBoundsException.class, addCommand::handleCommand);
    }

    @Test
    public void addCommand_wrongQuantityFormat_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("Book /quantity p", listOfBooks, new File("books.txt"));
        assertThrows(IndexOutOfBoundsException.class, addCommand::handleCommand);
    }
    @Test
    public void addCommand_maxIntNumber_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("Book /quantity 10000", listOfBooks, new File("books.txt"));
        assertThrows(IndexOutOfBoundsException.class, addCommand::handleCommand);
    }
    @Test
    public void addCommand_negativeQuantity_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("Book /quantity -1", listOfBooks, new File("books.txt"));
        assertThrows(IndexOutOfBoundsException.class, addCommand::handleCommand);
    }

}
