package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyListException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BorrowCommandTest {
    private ArrayList<Book> listOfBooks;
    private File bookDataFile;
    private Book availableBook;
    private Book borrowedBook;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        bookDataFile = new File("testBooks.txt"); // Ensure this file exists or is mocked
        availableBook = new Book("Available Book");
        borrowedBook = new Book("Borrowed Book");
        borrowedBook.setBorrowed(); // Assuming this method exists and marks the book as borrowed
        listOfBooks.add(availableBook);
        listOfBooks.add(borrowedBook);
        System.setOut(new PrintStream(outContent)); // Redirect System.out to capture console output
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut); // Restore original System.out
    }

    @Test
    public void borrowCommand_availableBook_bookIsBorrowed() {
        String[] commandParts = {"borrow", "Available Book"};
        BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        assertFalse(availableBook.isAvailable());
        assertTrue(availableBook.getIsBorrowed()); // Check if the book's status is borrowed
        assertTrue(outContent.toString().contains("Borrowed Available Book!"));
    }

    @Test
    public void borrowCommand_borrowedBook_bookBorrowingFails() {
        String[] commandParts = {"borrow", "Borrowed Book"};
        BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        assertTrue(borrowedBook.getIsBorrowed()); // Check if the book's status is still borrowed
        assertTrue(outContent.toString().contains("Book is currently unavailable."));
    }

    @Test
    public void borrowCommand_nonexistentBook_bookNotFoundMessageDisplayed() {
        String[] commandParts = {"borrow", "Nonexistent Book"};
        BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        assertTrue(outContent.toString().contains("Book not found: Nonexistent Book"));
    }

    @Test
    public void borrowCommand_emptyList_throwsEmptyListException() {
        String[] commandParts = {"borrow", "Some Book"};
        listOfBooks.clear();
        BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile);

        assertThrows(EmptyListException.class, command::handleCommand);
    }
}
