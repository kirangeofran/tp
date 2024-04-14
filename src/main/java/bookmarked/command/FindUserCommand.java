package bookmarked.command;

import bookmarked.user.User;
import bookmarked.exceptions.EmptyUserListException;
import bookmarked.ui.Ui;

import java.util.ArrayList;

public class FindUserCommand extends Command {
    private ArrayList<User> listOfUsers;
    private String userName;

    /**
     * find user command handles the case where find /by user is called
     * splits the array to extract the wanted user
     * @param listOfUsers the list of users who have borrowed books
     * @param userName the name of the user we are searching for
     */

    public FindUserCommand(ArrayList<User> listOfUsers, String userName) {
        this.listOfUsers = listOfUsers;
        this.userName = userName;
    }

    /**
     * handles the command finduser
     * catches emptyuserlist if there are no users currently
     */

    @Override
    public void handleCommand() {
        try {
            printUsers();
        } catch (EmptyUserListException e) {
            Ui.printEmptyUserListMessage();
        }
    }

    /**
     * prints the wanted user and their list of books
     * iterates through the list of users to find user printed
     * iterates through each users list of books to print user followed by their books borrowed
     *
     * @throws EmptyUserListException if the list of users is empty
     */

    void printUsers() throws EmptyUserListException {
        boolean userFound = false;
        if (listOfUsers.isEmpty()) {
            throw new EmptyUserListException();
        }
        int userCount = 0;
        for (User user : listOfUsers) {
            if (user.getName().contains(userName)) {
                userFound = true;
                userCount ++;
                findUser(user, userCount);
            }
        }
        if (!userFound) {
            Ui.invalidUser();
        }
    }

    /**
     * Iterates through the list of users to find the matching user
     * @param user the wanted user
     */

    private void findUser(User user, int userCount) {
        System.out.println("User: " + user.getName());
        System.out.println("Borrowed Books: ");
        if (user.getUserBooks().isEmpty()) {
            System.out.println("None");
        } else {
            PrintUserBooksCommand.printUserBooks(user, userCount, listOfUsers);
        }
    }
}

