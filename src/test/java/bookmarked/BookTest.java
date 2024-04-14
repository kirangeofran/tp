package bookmarked;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

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
}

