/*
package bookmarked.command;

import bookmarked.Book;
import bookmarked.User;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.WrongInputFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class BorrowCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private Book testBook;
    private Book borrowedBook;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = new File("testBooks.txt");

        testBook = new Book("Test Book");
        borrowedBook = new Book("Borrowed Book");
        listOfBooks.add(testBook);
        listOfBooks.add(borrowedBook);
        borrowedBook.borrowBook(LocalDate.now(), Period.ofWeeks(2));

        User user = new User("Test User");
        listOfUsers.add(user);

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void borrowCommand_availableBookByName_bookIsBorrowed() {
        String[] commandParts = {"borrow", "Test Book /by Test User"};
        String newItem = "borrow Test Book /by Test User";
        try {
            BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile, listOfUsers, newItem);
            command.handleCommand();
            Assertions.assertFalse(testBook.isAvailable());
            Assertions.assertTrue(outContent.toString().contains("Borrowed Test Book by Test User!"));
        } catch (EmptyArgumentsException | WrongInputFormatException e) {
            Assertions.fail("No exception should be thrown for valid input");
        }
    }

    @Test
    public void borrowCommand_availableBookByIndex_bookIsBorrowed() {
        String[] commandParts = {"borrow", "1 /by Test User"};
        String newItem = "borrow 1 /by Test User";
        try {
            BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile, listOfUsers, newItem);
            command.handleCommand();
            Assertions.assertFalse(testBook.isAvailable());
            Assertions.assertTrue(outContent.toString().contains("Borrowed Test Book by Test User!"));
        } catch (EmptyArgumentsException | WrongInputFormatException e) {
            Assertions.fail("No exception should be thrown for valid input");
        }
    }

    @Test
    public void borrowCommand_borrowedBookByName_borrowingFails() {
        String[] commandParts = {"borrow", "Borrowed Book /by Test User"};
        String newItem = "borrow Borrowed Book /by Test User";
        try {
            BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile, listOfUsers, newItem);
            command.handleCommand();
            Assertions.assertTrue(borrowedBook.getIsBorrowed());
            Assertions.assertTrue(outContent.toString().contains("Book is currently unavailable."));
        } catch (EmptyArgumentsException | WrongInputFormatException e) {
            Assertions.fail("No exception should be thrown for valid input");
        }
    }

    @Test
    public void borrowCommand_nonexistentBook_bookNotFoundMessageDisplayed() {
        String[] commandParts = {"borrow", "Nonexistent Book /by Test User"};
        String newItem = "borrow Nonexistent Book /by Test User";
        try {
            BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile, listOfUsers, newItem);
            command.handleCommand();
            Assertions.assertTrue(outContent.toString().contains("Book not found: Nonexistent Book"));
        } catch (EmptyArgumentsException | WrongInputFormatException e) {
            Assertions.fail("No exception should be thrown for valid input");
        }
    }

    @Test
    public void borrowCommand_emptyList_displaysEmptyListMessage() {
        String[] commandParts = {"borrow", "Some Book /by Test User"};
        String newItem = "borrow Some Book /by Test User";
        listOfBooks.clear();
        try {
            BorrowCommand command = new BorrowCommand(commandParts, listOfBooks, bookDataFile, listOfUsers, newItem);
            command.handleCommand();
            Assertions.assertTrue(outContent.toString().contains("The list of books is empty."));
        } catch (EmptyArgumentsException | WrongInputFormatException e) {
            Assertions.fail("No exception should be thrown for valid input");
        }
    }
}
*/
