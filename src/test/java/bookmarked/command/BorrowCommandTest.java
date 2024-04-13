package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.InvalidStringException ;
import bookmarked.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class BorrowCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File dummyBookDataFile;
    private File dummyUserDataFile;

    @BeforeEach
    public void setup() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        dummyBookDataFile = new File("dummyBookData.txt");
        dummyUserDataFile = new File("dummyUserData.txt");
    }

    @Test
    public void borrowCommand_emptyBookList_throwsEmptyListException() {
        BorrowCommand borrowCommand = new BorrowCommand(listOfBooks, dummyBookDataFile,
                listOfUsers, "borrow 1 /by Alice", dummyUserDataFile);
        assertThrows(EmptyListException.class, borrowCommand::handleCommand);
    }

    @Test
    public void borrowCommand_emptyArguments_throwsEmptyArgumentsException() {
        listOfBooks.add(new Book("Java Basics"));
        BorrowCommand borrowCommand = new BorrowCommand(listOfBooks, dummyBookDataFile,
                listOfUsers, "borrow /by", dummyUserDataFile);
        assertThrows(EmptyArgumentsException.class, borrowCommand::handleCommand);
    }

    @Test
    public void borrowCommand_bookNotFound_throwsBookNotFoundException() {
        listOfBooks.add(new Book("Java Basics"));
        BorrowCommand borrowCommand = new BorrowCommand(listOfBooks, dummyBookDataFile,
                listOfUsers, "borrow NonExistingBook /by Alice", dummyUserDataFile);
        assertThrows(BookNotFoundException.class, borrowCommand::handleCommand);
    }

    @Test
    public void borrowCommand_bookAlreadyBorrowed_printsAlreadyBorrowedMessage() {

        Book book = new Book("Java Basics");
        book.borrowBook(LocalDate.now(), Period.ofWeeks(2)); // Simulate borrowing the book
        listOfBooks.add(book);
        User alice = new User("Alice", listOfBooks);
        listOfUsers.add(alice);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String commandString = "borrow Java Basics /by Alice";
        BorrowCommand borrowCommand = new BorrowCommand(listOfBooks,
                dummyBookDataFile, listOfUsers, commandString, dummyUserDataFile);
        borrowCommand.handleCommand();

        String expectedOutput = "has already borrowed this book";
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }

    @Test
    public void borrowCommand_missingBookTitle_throwsInvalidStringException() {
        BorrowCommand borrowCommand = new BorrowCommand(listOfBooks, dummyBookDataFile,
                listOfUsers, "borrow /by Alice", dummyUserDataFile);
        assertThrows(InvalidStringException.class, borrowCommand::handleCommand);
    }

    @Test
    public void borrowCommand_missingByKeyword_throwsInvalidStringException() {
        listOfBooks.add(new Book("Java Basics"));
        BorrowCommand borrowCommand = new BorrowCommand(listOfBooks, dummyBookDataFile,
                listOfUsers, "borrow Java Basics Alice", dummyUserDataFile);
        assertThrows(InvalidStringException.class, borrowCommand::handleCommand);
    }


}
