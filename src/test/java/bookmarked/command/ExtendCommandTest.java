package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.storage.UserStorage;
import bookmarked.ui.Ui;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.time.LocalDate;
import java.time.Period ;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExtendCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;
    private File bookDataFile;
    private File userDataFile;
    private Book borrowedBook;
    private User currentUser;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = new File("testBooks.txt");
        userDataFile = new File("testUsers.txt");
        borrowedBook = new Book("Borrowed Book");
        Period borrowPeriod = Period.ofDays(14); // Assuming you want to borrow for 14 days
        borrowedBook.borrowBook(LocalDate.now(), borrowPeriod);
        currentUser = new User("Alice", listOfBooks);
        listOfBooks.add(borrowedBook);
        listOfUsers.add(currentUser);
        System.setOut(new PrintStream(outContent)); // Set the standard output to the outContent stream
    }

    @Test
    public void extendCommand_borrowedBook_extensionSuccessful() {
        currentUser.borrowBook(0, LocalDate.now(), LocalDate.now().plusWeeks(2));
        String commandString = "extend Borrowed Book /by Alice";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        String expectedOutput = "The borrowing period for 'Borrowed Book' has been successfully extended by one week";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected message about successful extension not found.");
    }

    @Test
    public void extendCommand_bookNotBorrowed_displaysNotBorrowedMessage() {
        Book notBorrowedBook = new Book("Not Borrowed Book");
        listOfBooks.add(notBorrowedBook);
        String commandString = "extend Not Borrowed Book /by Alice";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        String expectedOutput = "Alice has not borrowed this book. Nothing to extend.";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected message about book not being borrowed not found.");
    }

    @Test
    public void extendCommand_bookNotFound_printsBookNotFoundExceptionMessage() {
        String commandString = "extend Nonexistent Book /by Alice";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        String expectedOutput = "The book does not exist; try adding it to the library first.";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected message about non-existing book not found.");
    }

    @Test
    public void extendCommand_noArgumentsProvided_printsEmptyArgumentsMessage() {
        String commandString = "extend /by Alice";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        String expectedOutput = "Please type in the correct arguments.";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected message about empty arguments not found.");
    }

    @Test
    public void extendCommand_noUserProvided_printsUserNotFoundExceptionMessage() {
        String commandString = "extend Borrowed Book /by ";
        ExtendCommand command = new ExtendCommand(commandString, listOfBooks, bookDataFile, listOfUsers, userDataFile);
        command.handleCommand();

        String expectedOutput = "Please type in the correct arguments.";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected message about empty arguments not found.");
    }
}
