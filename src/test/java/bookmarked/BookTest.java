package bookmarked;

import bookmarked.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList ;

import bookmarked.command.ExtendCommand ;


public class BookTest {

    @TempDir
    Path tempDir;  // JUnit creates and cleans up a temporary directory

    @Test
    public void getBookDescription_returnsBookDescription() {
        Book testBook = new Book("Test");
        assertEquals("Test", testBook.getName());
    }

    @Test
    public void getBorrowedStatus_unBorrowed_returnsAvailable() {
        Book book = new Book("Some description");
        assertEquals("available", book.getBorrowedStatus());
    }

    @Test
    public void getBorrowedStatus_borrowed_returnFormattedString() {
        Book testBook = new Book("Test Book");
        testBook.isBorrowed = true;
        assertEquals(", borrowed", testBook.getBorrowedStatus());
    }

    @Test
    public void toString_descriptionAndBorrowed_returnFormattedString() {
        Book testBook = new Book("The Book Thief");
        testBook.borrowBook(LocalDate.now(), Period.ofDays(14)); // Set a borrow date to avoid NullPointerException
        // Update the expected string to what should be output by the toString() method
        String expectedOutput = String.format("The Book Thief, borrowed on: %s, due on: %s",
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        assertEquals(expectedOutput, testBook.toString());
    }


    @Test
    public void toString_descriptionAndReturned_returnNonFormattedString() {
        Book testBook = new Book("The Book Thief");
        testBook.setBorrowed(); // Borrow the book first
        testBook.setReturned(); // Now, return the book
        String expectedOutput = "The Book Thief available";
        assertEquals(expectedOutput, testBook.toString());
    }


    @Test
    public void extendDueDate_bookExtended_returnsExtendedDate() {
        // Initialize the list of books with a test book already borrowed
        ArrayList<Book> listOfBooks = new ArrayList<>();
        ArrayList<User> listOfUsers = new ArrayList<>();
        Book testBook = new Book("Test Book");
        testBook.borrowBook(LocalDate.now().minusDays(10), Period.ofDays(14));
        listOfBooks.add(testBook);

        // Create a temporary file to act as the book data file
        File bookDateFile = tempDir.resolve("testBooks.txt").toFile();
        File userDataFile = tempDir.resolve("testUsers.txt").toFile();

        // Split the input as the Parser would do and pass it to the ExtendCommand
        String commandString = "extend Borrowed Book /by Alice";
        String[] commandParts = {"extend", "Test Book"};
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDateFile, listOfUsers, userDataFile);
        command.handleCommand();

        // Calculate the expected due date after extension
        LocalDate expectedDueDate = LocalDate.now().minusDays(10).plusDays(14).plusDays(7); // 7 days extension
        LocalDate actualDueDate = testBook.getReturnDate();

        // Check if the actual due date matches the expected due date
        assertEquals(expectedDueDate, actualDueDate, "The due date should be extended by 7 days.");
    }
}

