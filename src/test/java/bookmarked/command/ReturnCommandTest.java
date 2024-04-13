package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.exceptions.EmptyListException ;
import bookmarked.exceptions.EmptyArgumentsException ;
import bookmarked.exceptions.BookNotFoundException ;
import bookmarked.exceptions.UserNotFoundException ;
import bookmarked.exceptions.IndexOutOfListBounds;
import java.time.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File dummyBookDataFile;
    private File dummyUserDataFile;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        dummyBookDataFile = new File("dummyBookData.txt"); // Assume these are set up for testing
        dummyUserDataFile = new File("dummyUserData.txt");
    }

    @AfterEach
    public void tearDown() {
        // Clean up after each test
        listOfBooks.clear();
        listOfUsers.clear();
    }

    @Test
    public void returnCommand_bookSuccessfullyReturned() {
        Book book = new Book("Java Basics");
        User user = new User("Alice", new ArrayList<>());
        // Inside your test method where you're setting up the book and user:
        LocalDate borrowDate = LocalDate.now(); // Capture the current date
        Period borrowPeriod = Period.ofWeeks(2); // Define the borrow period
// Assuming the Book class has a borrowBook method with the following signature:
// public void borrowBook(LocalDate borrowDate, Period borrowPeriod)
        book.borrowBook(borrowDate, borrowPeriod); // Correct method call

        listOfBooks.add(book);
        listOfUsers.add(user);
        user.borrowBook(0, LocalDate.now(), LocalDate.now().plusWeeks(2)); // User borrows book at index 0

        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by Alice", listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        assertFalse(book.getIsBorrowed(), "Book should be marked as not borrowed after being returned.");
    }

    @Test
    public void returnCommand_emptyBookList_throwsEmptyListException() {
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by Alice", listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(EmptyListException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_emptyArguments_throwsEmptyArgumentsException() {
        listOfBooks.add(new Book("Java Basics"));
        ReturnCommand returnCommand = new ReturnCommand("return /by", listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(EmptyArgumentsException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_bookNotFound_throwsBookNotFoundException() {
        listOfBooks.add(new Book("Java Basics"));
        ReturnCommand returnCommand = new ReturnCommand("return NonExistingBook /by Alice", listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(BookNotFoundException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_userNotFound_throwsUserNotFoundException() {
        Book book = new Book("Java Basics");
        listOfBooks.add(book);
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by NonExistingUser", listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(UserNotFoundException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_indexOutOfBounds_throwsIndexOutOfListBounds() {
        Book book = new Book("Java Basics");
        listOfBooks.add(book);
        User user = new User("Alice", new ArrayList<>());
        listOfUsers.add(user);
        user.borrowBook(0, LocalDate.now(), LocalDate.now().plusWeeks(2)); // User borrows book at index 0

        // Passing an out-of-bounds index
        ReturnCommand returnCommand = new ReturnCommand("return 2 /by Alice", listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(IndexOutOfListBounds.class, returnCommand::handleCommand);
    }


}
