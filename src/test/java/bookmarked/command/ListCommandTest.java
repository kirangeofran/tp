package bookmarked.command;

import bookmarked.exceptions.EmptyArgumentsException;
import org.junit.jupiter.api.Test;
import bookmarked.Book;
import bookmarked.user.User;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import bookmarked.exceptions.EmptyListException;

public class ListCommandTest {

    @Test
    public void listCommand_emptyList_exceptionThrown(){
        ArrayList<Book> listOfBooks = new ArrayList<>();
        ArrayList<User> listOfUsers = new ArrayList<>();

        ListCommand listDefault = new ListCommand(listOfBooks, "list /sortby alphabetical", listOfUsers);
        listDefault.handleCommand();
        assertThrows(EmptyListException.class, listDefault::runListBlankCommand);

        ListCommand listDate = new ListCommand(listOfBooks, "list /sortby returndate", listOfUsers);
        listDate.handleCommand();
        assertThrows(EmptyListException.class, listDate::runListDateCommand);

        ListCommand listAlphabetical = new ListCommand(listOfBooks, "list /sortby alphabetical", listOfUsers);
        listAlphabetical.handleCommand();
        assertThrows(EmptyListException.class, listDate::runListAlphabeticalCommand);
    }


    @Test
    public void listCommand_emptyArguments_exceptionThrown(){
        ArrayList<Book> listOfBooks = new ArrayList<>();
        ArrayList<User> listOfUsers = new ArrayList<>();

        ListCommand listDefault = new ListCommand(listOfBooks, "list /sortby", listOfUsers);
        listDefault.handleCommand();
        assertThrows(EmptyArgumentsException.class, listDefault::parseCommand);
    }


}
