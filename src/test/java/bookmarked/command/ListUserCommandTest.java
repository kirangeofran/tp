package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyUserListException;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ListUserCommandTest {

    @Test
    public void listUserCommand_emptyUserList_exception(){
        ArrayList<User> listOfUsers = new ArrayList<>();
        ListUserCommand listUser = new ListUserCommand(listOfUsers);
        listUser.handleCommand();
        assertThrows(EmptyUserListException.class, listUser::checkUsersAndBooks);
    }

    @Test
    public void listCommand_emptyArguments_exceptionThrown(){
        ArrayList<Book> listOfBooks = new ArrayList<>();
        ArrayList<User> listOfUsers = new ArrayList<>();
        ListCommand listAll = new ListCommand(listOfBooks, "list /sortby", listOfUsers);
        listAll.handleCommand();
        assertThrows(EmptyArgumentsException.class, listAll::parseCommand);
    }


}
