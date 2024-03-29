package bookmarked.command;

import bookmarked.Book;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindCommandTest {
    @Test
    public void handleCommand_emptyArrayList_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<Book>();
        String userInput = "find ok";
        Command userCommand = new FindCommand(userInput, listOfBooks);

        try {
            userCommand.handleCommand();
        } catch (Exception e) {
            assertEquals("The list is empty, try adding a book first.", e.getMessage());
        }
    }

    @Test
    public void handleCommand_emptyKeyword_exceptionThrown() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        String userInput = "";
        Command userCommand = new FindCommand(userInput, listOfBooks);

        try {
            userCommand.handleCommand();
        } catch (Exception e) {
            assertEquals("keyword should not be empty", e.getMessage());
        }
    }


    @Test
    public void handleCommand_keywordNoMatch_numberOfBookFoundZero() {
        ArrayList<Book> listOfBook = new ArrayList<>();
        String userInput = "find nomatch";
        FindCommand userCommand = new FindCommand(userInput, listOfBook);
        userCommand.handleCommand();

        int numberOfBookFound = userCommand.getNumberOfBookFound();
        System.out.println("number of books: " + numberOfBookFound);
        assertEquals(0, numberOfBookFound);
    }
    
    @Test
    public void handleCommand_keywordMatch_numberOfBookFoundMoreThanZero() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        String userInput1 = "find book";
        String userInput2 = "find orld";
        listOfBooks.add(new Book("book1"));
        listOfBooks.add(new Book("book2"));
        listOfBooks.add(new Book ("hello world"));

        FindCommand userCommand1 = new FindCommand(userInput1, listOfBooks);
        userCommand1.handleCommand();
        int numberOfBookFound = userCommand1.getNumberOfBookFound();
        assertEquals(2, numberOfBookFound);

        FindCommand userCommand2 = new FindCommand(userInput2, listOfBooks);
        userCommand2.handleCommand();
        numberOfBookFound = userCommand2.getNumberOfBookFound();
        assertEquals(1, numberOfBookFound);
    }
}
