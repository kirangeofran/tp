package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BorrowCommandTest {
    private ArrayList<Book> listOfBooks;
    private ArrayList<User> listOfUsers;

    private static final Period DEFAULT_BORROW_PERIOD = Period.ofWeeks(2);
    private File bookDataFile;
    private File userDataFile;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        listOfBooks = new ArrayList<>();
        listOfUsers = new ArrayList<>();
        bookDataFile = new File("testBooks.txt");
        userDataFile = new File("testUsers.txt");
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(originalOut);
    }

    @Test
    public void borrowCommand_emptyBookList_printsEmptyListMessage() {
        String commandString = "borrow 1 /by Alice";
        new BorrowCommand(listOfBooks, bookDataFile, listOfUsers, commandString, userDataFile).handleCommand();
        String expectedOutput = "Please enter a book index that exists on the current list.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

   /* @Test
    public void borrowCommand_successfulBorrowByBookName_printsSuccessMessage() {
        listOfBooks.add(new Book("Java Basics"));
        User alice = new User("Alice", new ArrayList<>());
        listOfUsers.add(alice);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String commandString = "borrow Java Basics /by Alice";
        new BorrowCommand(listOfBooks, bookDataFile, listOfUsers, commandString, userDataFile).handleCommand();

        String actualOutput = outContent.toString().trim();
        System.setOut(originalOut);

        System.out.println("Actual output: " + actualOutput);

        LocalDate expectedReturnDate = LocalDate.now().plus(Period.ofWeeks(2));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedReturnDate = expectedReturnDate.format(formatter);


        String successMessage = "Borrowed Java Basics by Alice!";
        String returnPrompt = "Please return by " + formattedReturnDate + ".";

        assertTrue(actualOutput.contains(successMessage), "Expected borrow success message not found.");
        assertTrue(actualOutput.contains(returnPrompt), "Expected return prompt not found.");
    }*/



    @Test
    public void borrowCommand_nonExistingBook_printsBookNotFoundMessage() {
        String commandString = "borrow NonExistingBook /by Alice";
        new BorrowCommand(listOfBooks, bookDataFile, listOfUsers, commandString, userDataFile).handleCommand();
        String expectedOutput = "The book does not exist; try adding it to the library first.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }

    @Test
    public void borrowCommand_bookAlreadyBorrowed_noAvailableCopies() {

        Book book = new Book("Java Basics");
        book.borrowBook(LocalDate.now(), Period.ofWeeks(2)); // Alice borrows the book
        listOfBooks.add(book);

        User alice = new User("Alice", listOfBooks);
        listOfUsers.add(alice);

        String commandString = "borrow Java Basics /by John";

        new BorrowCommand(listOfBooks, bookDataFile, listOfUsers, commandString, userDataFile).handleCommand();

        String expectedOutput = "There are currently no available copies of the book in the inventory.";
        assertTrue(outContent.toString().contains(expectedOutput));
    }


    @Test
    public void borrowCommand_missingBookTitle_printsInvalidArgumentsMessage() {
        String commandString = "borrow /by Alice";
        new BorrowCommand(listOfBooks, bookDataFile, listOfUsers, commandString, userDataFile).handleCommand();
        String expectedOutput = "Please type in the correct arguments.";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected invalid arguments message not printed.");
    }

    @Test
    public void borrowCommand_missingByKeyword_printsInvalidFormatMessage() {
        listOfBooks.add(new Book("Java Basics"));
        String commandString = "borrow Java Basics Alice";
        new BorrowCommand(listOfBooks, bookDataFile, listOfUsers, commandString, userDataFile).handleCommand();
        String expectedOutput = "Please type in the correct arguments";
        assertTrue(outContent.toString().contains(expectedOutput),
                "Expected invalid format message not printed.");
    }

}
