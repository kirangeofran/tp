//package bookmarked.command;
//
//import bookmarked.Book;
//import bookmarked.exceptions.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AddCommandTest {
//    private static final String BOOK_FILE_PATH = "./testBooks.txt";
//    private static final String USER_FILE_PATH = "./testUser.txt";
//
//    private static File bookDataFile;
//    private static ArrayList<Book> listOfBooks;
//
//    @BeforeEach
//    public void init() {
//        bookDataFile = new File(BOOK_FILE_PATH);
//        listOfBooks = new ArrayList<>();
//    }
//
//    @Test
//    public void processAddCommand_emptyArguments_exceptionThrown() {
//        String userInput = "";
//        String[] splitInput = userInput.split(" ");
//        AddCommand addCommand = new AddCommand("", listOfBooks, bookDataFile);
//        assertThrows(EmptyArgumentsException.class, () -> {
//            addCommand.processAddCommand(splitInput);
//        });
//    }
//
//    @Test
//    public void setQuantityToAdd_wrongQuantityFormat_exceptionThrown() {
//        AddCommand addCommand = new AddCommand("Book /quantity p", listOfBooks, bookDataFile);
//        assertThrows(NumberFormatException.class, addCommand::setQuantityToAdd);
//    }
//    @Test
//    public void setQuantityToAdd_maxIntNumber_exceptionThrown() {
//        AddCommand addCommand = new AddCommand("Book /quantity 10000", listOfBooks, bookDataFile);
//        assertThrows(MaxIntNumberException.class, addCommand::setQuantityToAdd);
//    }
//    @Test
//    public void setQuantityToAdd_negativeQuantity_exceptionThrown() {
//        String newItem = "Book /quantity -1";
//        AddCommand addCommand = new AddCommand(newItem, listOfBooks, bookDataFile);
//        assertThrows(NegativeQuantityException.class, addCommand::setQuantityToAdd);
//    }
//    @Test
//    public void runAddCommand_invalidString_exceptionThrown() {
//        AddCommand addCommand = new AddCommand("|", listOfBooks, bookDataFile);
//        assertThrows(InvalidStringException.class, addCommand::runAddCommand);
//    }
//
//
//}
