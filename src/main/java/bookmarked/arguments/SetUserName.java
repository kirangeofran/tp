package bookmarked.arguments;

import bookmarked.storage.UserStorage;
import bookmarked.user.User;
import bookmarked.Book;
import bookmarked.exceptions.UserNotFoundException;


import java.io.File;
import java.util.ArrayList;


public class SetUserName {
    private final String userName;
    private final ArrayList<User> listOfUsers;


    public SetUserName(String userName, ArrayList<User> listOfUsers) {
        this.userName = userName;
        this.listOfUsers = listOfUsers;
    }

    public User checkUserNameValidity() throws UserNotFoundException {
        for (User user : listOfUsers) {
            if (user.getName().matches(this.userName)) {
                return user;
            }
        }
        throw new UserNotFoundException();
    }

    public User checkBorrowUserNameValidity(ArrayList<Book> listOfBooks, File userDataFile) {
        for (User user : listOfUsers) {
            if (user.getName().matches(this.userName)) {
                return user;
            }
        }

        // If user not found, create a new user and add the borrowed book
        User newUser = new User(userName, listOfBooks);
        listOfUsers.add(newUser);
        UserStorage.writeUserToTxt(userDataFile, listOfUsers);
        return newUser;
    }



}
