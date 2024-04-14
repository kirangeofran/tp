package bookmarked.command;

import bookmarked.Book;
import bookmarked.exceptions.EmptyUserListException;
import bookmarked.user.User;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindUserCommandTest {

    @Test
    public void findUserCommand_emptyUserList_exception() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        FindUserCommand findUser = new FindUserCommand(listOfUsers, "Tom");
        assertThrows(EmptyUserListException.class, findUser::printUsers);
    }

    @Test
    public void findUserCommand_userNotFound() throws EmptyUserListException {
        ByteArrayOutputStream outputWord = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputWord));
        ArrayList<Book> listOfBooks = new ArrayList<>();
        ArrayList<User> userList = new ArrayList<>();
        User a = new User("Tom", listOfBooks);
        userList.add(a);
        FindUserCommand findUserCommand = new FindUserCommand(userList, "Tim");
        findUserCommand.printUsers();
        assertEquals("user not found", outputWord.toString().trim());
    }
}
