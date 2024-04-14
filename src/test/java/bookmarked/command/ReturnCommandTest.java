package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.storage.BookStorage;
import bookmarked.storage.UserStorage;
import bookmarked.ui.Ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReturnCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File dummyBookDataFile;
    private File dummyUserDataFile;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        dummyBookDataFile = new File("./testBookData.txt");
        dummyUserDataFile = new File("./testUserData.txt");
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        listOfBooks.clear();
        listOfUsers.clear();
        outContent.reset();
        System.setOut(originalOut);
    }

    @Test
    public void returnCommand_bookSuccessfullyReturned() {
        // Arrange
        Book book = new Book("Java Basics");
        listOfBooks.add(book);
        int bookIndex = listOfBooks.indexOf(book);
        User user = new User("Alice", listOfBooks);
        listOfUsers.add(user);

        // Set the borrow and return dates for the book to be in the past to simulate an overdue return
        LocalDate borrowDate = LocalDate.now().minusDays(30); // Borrowed 30 days ago
        LocalDate returnDueDate = borrowDate.plusWeeks(2); // Should have been returned 2 weeks ago
        user.borrowBook(bookIndex, borrowDate, returnDueDate);

        // Capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        // Assert
        String returnMessage = "Returned Java Basics!";
        String actualOutput = outContent.toString().trim();

        assertTrue(actualOutput.contains(returnMessage), "Expected return message not found.");

        System.setOut(System.out);
    }



    @Test
    public void returnCommand_emptyBookList_printsEmptyListMessage() {
        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "The book does not exist; try adding it to the library first.";
        assertTrue(outContent.toString().trim().contains(expectedMessage), "Expected empty list message not found.");
    }

    @Test
    public void returnCommand_emptyArguments_printsEmptyArgumentsMessage() {
        listOfBooks.add(new Book("Java Basics"));

        ReturnCommand returnCommand = new ReturnCommand("return /by", listOfBooks,
                dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "Please type in the correct arguments";
        assertTrue(outContent.toString().trim().contains(expectedMessage),
                "Expected empty arguments message not found.");
    }

    @Test
    public void returnCommand_bookNotFound_printsBookNotFoundMessage() {
        listOfBooks.add(new Book("Java Basics"));

        ReturnCommand returnCommand = new ReturnCommand("return NonExistingBook /by Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "The book does not exist; try adding it to the library first.";
        assertTrue(outContent.toString().trim().contains(expectedMessage),
                "Expected book not found message not found.");
    }

    @Test
    public void returnCommand_userNotFound_printsUserNotFoundMessage() {
        Book book = new Book("Java Basics");
        listOfBooks.add(book);

        ReturnCommand returnCommand = new ReturnCommand("return Java Basics /by NonExistingUser",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "No such user exists. Please input the name of an existing user instead.";
        assertTrue(outContent.toString().trim().contains(expectedMessage),
                "Expected user not found message not found.");
    }

    @Test
    public void returnCommand_indexOutOfBounds_printsOutOfBoundsMessage() {
        Book book = new Book("Java Basics");
        listOfBooks.add(book);
        int bookIndex = 0;

        User user = new User("Alice", listOfBooks);
        listOfUsers.add(user);
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDueDate = borrowDate.plusWeeks(2);
        user.borrowBook(bookIndex, borrowDate, returnDueDate);

        ReturnCommand returnCommand = new ReturnCommand("return 2 /by Alice", listOfBooks,
                dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "Please enter a book index that exists on the current list.";
        assertTrue(outContent.toString().trim().contains(expectedMessage),
                "Expected out of bounds message not found.");
    }


    @Test
    public void returnCommand_missingBookTitle_printsEmptyArgumentsMessage() {
        ReturnCommand returnCommand = new ReturnCommand("return /by Alice", listOfBooks,
                dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "Please type in the correct arguments.";
        assertTrue(outContent.toString().trim().contains(expectedMessage),
                "Expected empty arguments message not found.");
    }

    @Test
    public void returnCommand_missingByKeyword_printsInvalidStringExceptionMessage() {
        listOfBooks.add(new Book("Java Basics"));
        User alice = new User("Alice", new ArrayList<>());
        listOfUsers.add(alice);

        ReturnCommand returnCommand = new ReturnCommand("return Java Basics Alice",
                listOfBooks, dummyBookDataFile, listOfUsers, dummyUserDataFile);
        returnCommand.handleCommand();

        String expectedMessage = "Please type in the correct arguments.";
        assertTrue(outContent.toString().trim().contains(expectedMessage),
                "Expected invalid command format message not found.");
    }
}

