package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.BookNotBorrowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExtendCommandTest {
    private ArrayList<Book> listOfBooks;
    private File bookDataFile; // Ensure this file exists or is mocked
    private Book borrowedBook;
    private Book notBorrowedBook;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        bookDataFile = new File("testBooks.txt"); // Ensure this file exists or is mocked
        borrowedBook = new Book("Borrowed Book");
        borrowedBook.borrowBook(LocalDate.now(), Period.ofWeeks(2)); // Already borrowed
        notBorrowedBook = new Book("Not Borrowed Book"); // Not borrowed
        listOfBooks.add(borrowedBook);
        listOfBooks.add(notBorrowedBook);
    }

    @Test
    public void extendCommand_borrowedBook_extensionSuccessful() {
        String[] commandParts = {"extend", "Borrowed Book"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        // Assuming extendDueDate() extends the due date by 7 days
        LocalDate expectedReturnDate = LocalDate.now().plusWeeks(2).plusDays(7);
        assertEquals(expectedReturnDate, borrowedBook.getReturnDate());
    }

    @Test
    public void extendCommand_notBorrowedBook_throwsBookNotBorrowedException() {
        String[] commandParts = {"extend", "Not Borrowed Book"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);

        assertThrows(BookNotBorrowedException.class, command::handleCommand);
    }

    @Test
    public void extendCommand_bookNotFound_throwsBookNotFoundException() {
        String[] commandParts = {"extend", "Nonexistent Book"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);

        assertThrows(BookNotFoundException.class, command::handleCommand);
    }

    @Test
    public void extendCommand_noBookNameProvided_throwsBookNotFoundException() {
        String[] commandParts = {"extend"};
        ExtendCommand command = new ExtendCommand(commandParts, listOfBooks, bookDataFile);

        assertThrows(BookNotFoundException.class, command::handleCommand);
    }
}
