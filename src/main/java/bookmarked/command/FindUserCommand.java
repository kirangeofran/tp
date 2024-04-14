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
     *
     * @param listOfUsers the list of users who have borrowed books
     * @param userName    the name of the user we are searching for
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
     * iterates through the list of users to find wanted user
     * stores matching users into a new array
     * iterates through each users list of books to print user
     *
     * @throws EmptyUserListException if the list of users is empty
     */

    void printUsers() throws EmptyUserListException {
        if (listOfUsers.isEmpty()) {
            throw new EmptyUserListException();
        }
        ArrayList<User> foundUsers = new ArrayList<>();
        for (User user : listOfUsers) {
            if (user.getName().contains(userName)) {
                foundUsers.add(user);
            }
        }
        if (foundUsers.isEmpty()) {
            Ui.invalidUser(userName);
        } else {
            findUser(foundUsers);
        }
    }

    /**
     * Iterates through the list of matching users to print the matching users
     * For every matching user, print their books
     * @param foundUsers the array list of wanted users
     */

    private void findUser(ArrayList<User> foundUsers) {
        int userCount = 0;
        for (User user : foundUsers) {
            System.out.println("User: " + user.getName());
            System.out.println("Borrowed Books: ");
            if (user.getUserBooks().isEmpty()) {
                System.out.println("None");
            } else {
                PrintUserBooksCommand.printUserBooks(user, userCount, foundUsers);
            }
            userCount++;
        }
    }
}

