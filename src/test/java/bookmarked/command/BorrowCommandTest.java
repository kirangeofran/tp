package bookmarked.command;

import bookmarked.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        borrowedBook = new Book("Borrowed Book"); // Adjusted for simplicity
        listOfBooks.add(availableBook);
        listOfBooks.add(borrowedBook);
        borrowedBook.borrowBook(LocalDate.now(),Period.ofWeeks(2)); // Corrected to use borrow method with a Period

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

        // Assuming isAvailable() returns false when the book is borrowed
        assertFalse(availableBook.isAvailable());
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
    public void borrowCommand_emptyList_displaysEmptyListMessage() {
        String[] commandParts = {"borrow", "Some Book"};
        listOfBooks.clear();
        BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        // Adjust this expected message to match what's actually printed by your Ui.printEmptyListMessage() method
        String expectedMessage = "The list is empty, try adding a book first.";
        assertTrue(outContent.toString().contains(expectedMessage));
    }
}
