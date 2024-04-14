package bookmarked.command;

import bookmarked.Book;

import java.util.ArrayList;

import bookmarked.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private Command userCommand;
    private String userInput;

    @BeforeEach
    public void init() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
    }
    @Test
    public void handleCommand_emptyArrayList_exceptionThrown() {
        userInput = "find /by book ok";
        userCommand = new FindCommand(userInput, listOfBooks, listOfUsers);

        try {
            userCommand.handleCommand();
        } catch (Exception e) {
            assertEquals("The list is empty, try adding a book first.", e.getMessage());
        }
    }

    @Test
    public void handleCommand_emptyKeyword_exceptionThrown() {
        userInput = "";
        userCommand = new FindCommand(userInput, listOfBooks, listOfUsers);

        try {
            userCommand.handleCommand();
        } catch (Exception e) {
            assertEquals("keyword should not be empty", e.getMessage());
        }
    }


    @Test
    public void handleCommand_keywordNoMatch_numberOfBookFoundZero() {
        ArrayList<Book> listOfBook = new ArrayList<>();
        userInput = "find /by book nomatch";
        FindCommand userCommand = new FindCommand(userInput, listOfBook, listOfUsers);
        userCommand.handleCommand();

        int numberOfBookFound = userCommand.getNumberOfBookFound();
        System.out.println("number of books: " + numberOfBookFound);
        assertEquals(0, numberOfBookFound);
    }

    @Test
    public void handleCommand_keywordMatch_numberOfBookFoundMoreThanZero() {
        String userInput1 = "find /by book book";
        String userInput2 = "find /by book orld";
        listOfBooks.add(new Book("book1"));
        listOfBooks.add(new Book("book2"));
        listOfBooks.add(new Book ("hello world"));

        FindCommand userCommand1 = new FindCommand(userInput1, listOfBooks, listOfUsers);
        userCommand1.handleCommand();
        int numberOfBookFound = userCommand1.getNumberOfBookFound();
        assertEquals(2, numberOfBookFound);

        FindCommand userCommand2 = new FindCommand(userInput2, listOfBooks, listOfUsers);
        userCommand2.handleCommand();
        numberOfBookFound = userCommand2.getNumberOfBookFound();
        assertEquals(1, numberOfBookFound);
    }
}
