package bookmarked.arguments;

import bookmarked.user.User;
import bookmarked.exceptions.UserNotFoundException;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class SetUserName {
    private final String userName;
    private final ArrayList<User> listOfUsers;


    public SetUserName(String userName, ArrayList<User> listOfUsers) {
        this.userName = userName;
        this.listOfUsers = listOfUsers;
    }

    public User checkUserNameValidity() throws UserNotFoundException {
        String userString = userName;
        for (User user : listOfUsers) {
            if (user.getName().matches(userString)) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }

}
