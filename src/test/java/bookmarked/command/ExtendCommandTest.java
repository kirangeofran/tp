package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import java.io.File;
import java.time.Period;
import bookmarked.exceptions.InvalidStringException;
import bookmarked.exceptions.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExtendCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;
    private Book borrowedBook;
    private User currentUser;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = new File("testBooks.txt");
        userDataFile = new File("testUsers.txt");
        borrowedBook = new Book("Borrowed Book");
        borrowedBook.borrowBook(LocalDate.now(), Period.ofWeeks(2)); // Already borrowed
        currentUser = new User("Alice", listOfBooks);
        listOfBooks.add(borrowedBook);
        listOfUsers.add(currentUser);
    }

    @Test
    public void extendCommand_borrowedBook_extensionSuccessful() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        currentUser.borrowBook(0, LocalDate.now(), LocalDate.now().plusWeeks(2));
        String commandString = "extend Borrowed Book /by Alice";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        System.setOut(originalOut);
        String output = outContent.toString();
        assertTrue(output.contains("Due date for 'Borrowed Book' has been extended by a week."));

        // Assuming extendDueDate() extends the due date by 7 days
        LocalDate expectedReturnDate = LocalDate.now().plusWeeks(2).plusDays(7);
        assertTrue(expectedReturnDate.equals(borrowedBook.getReturnDate()));
    }

    @Test
    public void extendCommand_bookNotBorrowed_displaysNotBorrowedMessage() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String commandString = "extend Not Borrowed Book /by Alice";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("has not borrowed this book"));
    }

    @Test
    public void extendCommand_bookNotFound_throwsBookNotFoundException() {
        assertThrows(BookNotFoundException.class, () -> {
            String commandString = "extend Nonexistent Book /by Alice";
            ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
            command.handleCommand();
        });
    }

    @Test
    public void extendCommand_noArgumentsProvided_throwsInvalidStringException() {
        assertThrows(InvalidStringException.class, () -> {
            String commandString = "extend";
            ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
            command.handleCommand();
        });
    }
}
