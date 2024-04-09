package bookmarked.command;

import bookmarked.Book;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;

import bookmarked.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.time.Period;

public class ReturnCommandTest {
    private static final String TEST_BOOK_FILE_PATH = "./testBooks.txt";
    private static final String TEST_USER_FILE_PATH = "./testUsers.txt";
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUser;
    private File bookDataFile;
    private File userDataFile;
    private Book borrowedBook;
    private Book notBorrowedBook;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUser = new ArrayList<>();

        // Assume bookDataFile is either a mock or set to a temporary/testing file path
        bookDataFile = new File(TEST_BOOK_FILE_PATH);
        userDataFile = new File(TEST_USER_FILE_PATH);

        borrowedBook = new Book("Borrowed Book");
        // Simulate that the book has been borrowed
        borrowedBook.borrowBook(LocalDate.now(), Period.ofDays(14));
        notBorrowedBook = new Book("Not Borrowed Book");

        listOfBooks.add(borrowedBook);
        listOfBooks.add(notBorrowedBook);

        System.setOut(new PrintStream(outContent)); // Redirect System.out to capture console output
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut); // Restore System.out to its original stream
    }

    @Test
    public void returnCommand_borrowedBookByName_bookIsReturned() {
        String[] commandParts = {"return", "Borrowed Book"};
        ReturnCommand command = new ReturnCommand(commandParts, listOfBooks, bookDataFile, listOfUser, userDataFile);

        command.handleCommand();

        assertFalse(borrowedBook.getIsBorrowed(), "Borrowed book should be marked as not borrowed after return.");
    }

    @Test
    public void returnCommand_borrowedBookByIndex_bookIsReturned() {
        // Index of the borrowed book in the list is 1 since it's the first item
        String[] commandParts = {"return", "1"};
        ReturnCommand command = new ReturnCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        assertFalse(borrowedBook.getIsBorrowed(),
                "Borrowed book should be marked as not borrowed after return using index.");
    }

    @Test
    public void returnCommand_notBorrowedBookByName_printsNotBorrowedMessage() {
        String[] commandParts = {"return", "Not Borrowed Book"};
        ReturnCommand command = new ReturnCommand(commandParts, listOfBooks, bookDataFile, listOfUser, userDataFile);

        command.handleCommand();

        String expectedOutput = "Book is not borrowed: Not Borrowed Book";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Should print a message that the book is not borrowed when returned by name.");
    }

    @Test
    public void returnCommand_notBorrowedBookByIndex_printsNotBorrowedMessage() {
        // Index of the not borrowed book in the list is 2 since it's the second item
        String[] commandParts = {"return", "2"};
        ReturnCommand command = new ReturnCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        String expectedOutput = "Book is not borrowed: Not Borrowed Book";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Should print a message that the book is not borrowed when returned by index.");
    }

    @Test
    public void returnCommand_bookNotFoundByName_printsNotFoundMessage() {
        String[] commandParts = {"return", "Nonexistent Book"};
        ReturnCommand command = new ReturnCommand(commandParts, listOfBooks, bookDataFile, listOfUser, userDataFile);

        command.handleCommand();

        String expectedOutput = "Book not found: Nonexistent Book";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Should print a message that the book is not found when using name.");
    }

    @Test
    public void returnCommand_bookNotFoundByIndex_printsNotFoundMessage() {
        // Using an index that is out of bounds for the list
        String[] commandParts = {"return", "10"};
        ReturnCommand command = new ReturnCommand(commandParts, listOfBooks, bookDataFile);

        command.handleCommand();

        String expectedOutput = "Book not found:";
        assertTrue(outContent.toString().trim().contains(expectedOutput),
                "Should print a message that the book is not found when using an invalid index.");
    }

}
