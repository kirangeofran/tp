package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.exceptions.EmptyListException;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.BookNotFoundException;
import bookmarked.exceptions.UserNotFoundException;
import bookmarked.exceptions.IndexOutOfListBounds;
import bookmarked.exceptions.InvalidStringException ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

public class ReturnCommandTest {
    private static final String TEST_BOOK_FILE_PATH = "./testBookData.txt";
    private static final String TEST_USER_FILE_PATH = "./testUserData.txt";
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File dummyBookDataFile;
    private File dummyUserDataFile;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        dummyBookDataFile = new File(TEST_BOOK_FILE_PATH);
        dummyUserDataFile = new File(TEST_USER_FILE_PATH);
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

        LocalDate borrowDate = LocalDate.now();
        Period borrowPeriod = Period.ofWeeks(2);
        book.borrowBook(borrowDate, borrowPeriod);

        listOfBooks.add(book);
        listOfUsers.add(user);
        user.borrowBook(0, LocalDate.now(), LocalDate.now().plusWeeks(2));
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        assertFalse(book.getIsBorrowed(), "Book should be marked as not borrowed after being returned.");
    }

    @Test
    public void returnCommand_emptyBookList_throwsEmptyListException() {
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(EmptyListException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_emptyArguments_throwsEmptyArgumentsException() {
        listOfBooks.add(new Book("Java Basics"));
        ReturnCommand returnCommand = new ReturnCommand("return /by", listOfBooks,
                dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(EmptyArgumentsException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_bookNotFound_throwsBookNotFoundException() {
        listOfBooks.add(new Book("Java Basics"));
        ReturnCommand returnCommand = new ReturnCommand("return NonExistingBook /by Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(BookNotFoundException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_userNotFound_throwsUserNotFoundException() {
        Book book = new Book("Java Basics");
        listOfBooks.add(book);
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by NonExistingUser",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(UserNotFoundException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_indexOutOfBounds_throwsIndexOutOfListBounds() {
        Book book = new Book("Java Basics");
        listOfBooks.add(book);
        User user = new User("Alice", new ArrayList<>());
        listOfUsers.add(user);
        user.borrowBook(0, LocalDate.now(), LocalDate.now().plusWeeks(2));

        ReturnCommand returnCommand = new ReturnCommand("return 2 /by Alice", listOfBooks,
                dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(IndexOutOfListBounds.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_missingBookTitle_throwsEmptyArgumentsException() {
        // When book title is missing after "return"
        ReturnCommand returnCommand = new ReturnCommand("return /by Alice", listOfBooks,
                dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(EmptyArgumentsException.class, returnCommand::handleCommand);
    }

    @Test
    public void returnCommand_missingByKeyword_throwsInvalidStringException() {
        // When the "/by" is missing in the command
        listOfBooks.add(new Book("Java Basics"));
        User alice = new User("Alice", listOfBooks);
        listOfUsers.add(alice);
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        assertThrows(InvalidStringException.class, returnCommand::handleCommand);
    }
}
