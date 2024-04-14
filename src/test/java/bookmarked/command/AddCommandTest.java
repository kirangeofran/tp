package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.*;
import bookmarked.user.User;
import bookmarked.ui.Ui;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AddCommandTest {

    @Test
    public void addCommand_emptyArguments_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        File bookDataFile = null;
        AddCommand addCommand = new AddCommand("", listOfBooks, bookDataFile);
        assertThrows(EmptyArgumentsException.class, addCommand::handleCommand);
    }

    @Test
    public void addCommand_wrongQuantityFormat_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("Book /quantity p", listOfBooks, new File("books.txt"));
        assertThrows(WrongQuantityException.class, addCommand::handleCommand);
    }
    @Test
    public void addCommand_maxIntNumber_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("Book /quantity 10000", listOfBooks, new File("books.txt"));
        assertThrows(MaxIntNumberException.class, addCommand::handleCommand);
    }
    @Test
    public void addCommand_negativeQuantity_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("Book /quantity -1", listOfBooks, new File("books.txt"));
        assertThrows(NegativeQuantityException.class, addCommand::handleCommand);
    }
    @Test
    public void addCommand_invalidString_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        AddCommand addCommand = new AddCommand("|", listOfBooks, new File("books.txt"));
        assertThrows(InvalidStringException.class, addCommand::handleCommand);
    }


}
