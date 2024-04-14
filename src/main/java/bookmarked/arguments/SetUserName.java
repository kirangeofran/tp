package bookmarked.arguments;

import bookmarked.storage.UserStorage;
import bookmarked.user.User;
import bookmarked.Book;
import bookmarked.exceptions.UserNotFoundException;
import bookmarked.exceptions.InvalidStringException;


import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


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

    public User checkBorrowUserNameValidity(ArrayList<Book> listOfBooks, File userDataFile)
            throws InvalidStringException {
        // If user not found, create a new user and add the borrowed book
        try {
            boolean hasSpecialCharacters = checkSpecialCharacters(this.userName);
            if (hasSpecialCharacters) {
                throw new InvalidStringException();
            }
        } catch (PatternSyntaxException e) {
            throw new InvalidStringException();
        }

        for (User user : listOfUsers) {
            if (user.getName().matches(this.userName)) {
                return user;
            }
        }

        User newUser = new User(userName, listOfBooks);
        listOfUsers.add(newUser);
        UserStorage.writeUserToTxt(userDataFile, listOfUsers);
        return newUser;
    }

    public static boolean checkSpecialCharacters(String bookName) {
        String pattern = "[^A-Za-z0-9 ]";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(bookName);
        return m.find();
    }

}
