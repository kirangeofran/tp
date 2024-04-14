package bookmarked.command;

import bookmarked.Book;
import bookmarked.user.User;
import bookmarked.exceptions.EmptyArgumentsException;
import bookmarked.exceptions.EmptyUserListException;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        ByteArrayOutputStream outputWord = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputWord));
        ArrayList<Book> listOfBooks = new ArrayList<>();
        ArrayList<User> userList = new ArrayList<>();
        User a = new User ("a", listOfBooks);
        userList.add(a);
        ListUserCommand listUserCommand = new ListUserCommand(userList);
        listUserCommand.handleCommand();
        assertEquals("List of Users and Borrowed Books:\nUser: a\nBorrowed Books: None\n",
                outputWord.toString());
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
