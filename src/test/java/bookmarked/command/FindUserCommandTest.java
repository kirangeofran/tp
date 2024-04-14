package bookmarked.command;

import bookmarked.exceptions.EmptyUserListException;
import bookmarked.user.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FindUserCommandTest {

    @Test
    public void findUserCommand_emptyUserList_exception() {
        ArrayList<User> listOfUsers = new ArrayList<>();
        FindUserCommand findUser = new FindUserCommand(listOfUsers, "Tom");
        assertThrows(EmptyUserListException.class, findUser::printUsers);
    }

}

