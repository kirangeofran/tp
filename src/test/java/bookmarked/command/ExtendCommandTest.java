package bookmarked.command;

import bookmarked.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtendCommandTest {
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;
    private Book borrowedBook;
    private Book notBorrowedBook;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        bookDataFile = new File("testBooks.txt");
        borrowedBook = new Book("Borrowed Book");
        borrowedBook.borrowBook(LocalDate.now(), Period.ofWeeks(2)); // Already borrowed
        notBorrowedBook = new Book("Not Borrowed Book"); // Not borrowed
        listOfBooks.add(borrowedBook);
        listOfBooks.add(notBorrowedBook);
    }

    @Test
    public void extendCommand_borrowedBook_extensionSuccessful() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] commandParts = {"extend", "Borrowed Book"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);
        command.handleCommand();

        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("Due date for 'Borrowed Book' has been extended by a week."));

        // Assuming extendDueDate() extends the due date by 7 days
        LocalDate expectedReturnDate = LocalDate.now().plusWeeks(2).plusDays(7);
        assertTrue(expectedReturnDate.equals(borrowedBook.getReturnDate()));
    }

    @Test
    public void extendCommand_notBorrowedBook_displaysNotBorrowedMessage() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] commandParts = {"extend", "Not Borrowed Book"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);
        command.handleCommand();

        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("Book not borrowed"));
    }

    @Test
    public void extendCommand_bookNotFound_displaysNotFoundMessage() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] commandParts = {"extend", "Nonexistent Book"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);
        command.handleCommand();

        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("Book not found"));
    }

    @Test
    public void extendCommand_noBookNameProvided_displaysNoBookNameMessage() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] commandParts = {"extend"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);
        command.handleCommand();

        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("Book not found"));
    }
}
