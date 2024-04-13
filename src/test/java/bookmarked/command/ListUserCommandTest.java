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
        assertThrows(EmptyUserListException.class, listUser::printUsersAndBooks);
    }

    @Test
    public void listUserCommand_nonEmptyUserList() {
        ArrayList<Book> listOfBooks = new ArrayList<>();
        User a = new User("a", listOfBooks);
        ArrayList<User> userList = new ArrayList<>();
        userList.add(a);
        ListUserCommand listUserCommand = new ListUserCommand(userList);
        listUserCommand.handleCommand();
        // assertEquals("List of Users and Borrowed Books:\nUser: a\nBorrowed Books:
        // None\n", listUserCommand::printUsersAndBooks);


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
