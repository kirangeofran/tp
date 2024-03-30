package bookmarked;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class BookTest {
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

}
